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
    public String generateToken(User user);
    public boolean verifyToken(String token, byte[] publicKeyByteData, User claimingUser);
}
