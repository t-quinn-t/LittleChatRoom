package com.server.service;

import com.server.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-07 12:24 p.m.
 */
@Service
public interface JWTAuthService {

    class JWTAuthServiceTokenPackage {
        private final String token;
        private final byte[] publicKeyByteData;

        public JWTAuthServiceTokenPackage(String token, byte[] publicKeyByteData) {
            this.token = token;
            this.publicKeyByteData = publicKeyByteData;
        }

        public String getToken() {
            return token;
        }

        public byte[] getPublicKey() {
            return publicKeyByteData;
        }
    }

    public JWTAuthServiceTokenPackage generateToken(User user);
    public boolean verifyToken(String token, byte[] publicKeyByteData, User claimingUser);
}
