package com.server.service.impl;

import com.auth0.jwt.algorithms.Algorithm;
import com.server.service.JWTAuthService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-07 12:24 p.m.
 */
public class JWTAuthServiceImpl implements JWTAuthService {

    private Logger logger = LoggerFactory.getLogger(JWTAuthServiceImpl.class);

    public String generateToken() {
        Security.addProvider(new BouncyCastleProvider());
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = (ECPublicKey) keyPair.getPublic();
            PrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot Generate Public/Private Keys with ECDSA");
        }


        return null;
    }

    public void verifyToken(String token) {

    }
}
