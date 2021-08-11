package com.server.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.server.dao.ECKeyPairDao;
import com.server.exception.UserTokenExpiredException;
import com.server.model.User;
import com.server.service.JWTAuthService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-07 12:24 p.m.
 */
@Component
@Qualifier("jwtservice")
public class JWTAuthServiceImpl implements JWTAuthService {

    private final ECGenParameterSpec ecGenParameterSpec;
    private final ECKeyPairDao keystore;
    private final Logger logger = LoggerFactory.getLogger(JWTAuthServiceImpl.class);

    private final String issuer = "littlechatroom";



    @Autowired
    public JWTAuthServiceImpl(ECKeyPairDao keystore) {
        this.keystore = keystore;
        this.ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        Security.addProvider(new BouncyCastleProvider());
    }

    public JWTAuthServiceTokenPackage generateToken(User user) throws RuntimeException {

        try {
            /* ===== ===== ===== Generating KeyPairs ===== ===== ===== */
            logger.info("Generating Public/Private key pairs");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA");
            keyPairGenerator.initialize(ecGenParameterSpec);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            /* ===== ===== ===== Storing KeyPairs ===== ===== ===== */
            logger.info("Storing new generated keypair to database");
            keystore.registerKeyPair(publicKey.getEncoded(), privateKey.getEncoded());

            /* ===== ===== ===== Generate Token ===== ===== ===== */
            logger.info("Generating new tokens using generated keypair");
            Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) publicKey, (ECPrivateKey) privateKey);
            String token = JWT.create()
                    .withIssuer(this.issuer)
                    .withExpiresAt(new Date(LocalDate.now().plusDays(1L).toEpochDay()))
                    .withClaim("uid", user.getUid())
                    .withClaim("uname", user.getName())
                    .withClaim("email", user.getEmail())
                    .sign(algorithm);
            logger.info("Successfully generated json web token");
            return new JWTAuthServiceTokenPackage(token, publicKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot generate public/private keys with ECDSA. No algorithm found.");
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("Cannot generate public/private keys using ECDSA, invalid algorithm: \n {}" + e.getMessage());
        } catch (NullPointerException e) {
            logger.error("Keypair dao error encountered: \n {}", e.getMessage());
        } catch (JWTCreationException e) {
            logger.error("Error happened when creating ecdsa256k token: \n {}", e.getMessage());
        }
        return null;
    }

    public boolean verifyToken(String token, byte[] publicKeyByteData, User claimingUser) throws RuntimeException {

        try {
            /* ===== ===== ===== Retrieving Private Key ===== ===== ===== */
            logger.info("Retrieving private key from database");
            byte[] privateKeyByteData = keystore.getPrivateKeyByteData(publicKeyByteData);

            /* ===== ===== ===== Restore keypair from byte data ===== ===== ===== */
            logger.info("Retrieving keypair from their byte data");
            KeyFactory factory = KeyFactory.getInstance("ECDSA");
            PrivateKey privateKey = factory.generatePrivate(new X509EncodedKeySpec(privateKeyByteData));
            PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(publicKeyByteData));

            /* ===== ===== ===== Verify token ===== ===== ===== */
            logger.info("Getting ");
            Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) publicKey, (ECPrivateKey) privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.issuer)
                    .withClaim("uid", claimingUser.getUid())
                    .withClaim("uname", claimingUser.getName())
                    .withClaim("email", claimingUser.getEmail())
                    .build();
            DecodedJWT decodedToken = verifier.verify(token);
            logger.info("Successfully verified token");
            return true;
        } catch (NullPointerException e) {
            logger.error("Cannot retrieve private key with given public key");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot retrieve public/private keys with ECDSA. No algorithm found.");
        } catch (InvalidKeySpecException e) {
            logger.error("Cannot retrieve specified key spec: \n {}", e.getMessage());
        } catch (JWTCreationException e) {
            logger.error("Cannot create jwt verifier instance: \n {}", e.getMessage());
        } catch (TokenExpiredException e) {
            // Special case where user token is expired
            
            /* ===== ===== ===== Remove Keypairs from keystore ===== ===== ===== */
            logger.info("Removing keypair from keystore as token is expired");
            keystore.deregisterKeyPair(publicKeyByteData);

            /* ===== ===== ===== Throw Exception to client ===== ===== ===== */
            throw new UserTokenExpiredException();
        } catch (JWTVerificationException e) {
            logger.error("Bad token");
        }
        return false;
    }
}
