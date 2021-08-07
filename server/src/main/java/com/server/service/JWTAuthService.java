package com.server.service;

import org.springframework.stereotype.Service;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-07 12:24 p.m.
 */
@Service
public interface JWTAuthService {
    public String generateToken();
    public void verifyToken(String token);
}
