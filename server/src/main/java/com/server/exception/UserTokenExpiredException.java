package com.server.exception;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-08 12:41 a.m.
 */
public class UserTokenExpiredException extends RuntimeException {

    public UserTokenExpiredException() {
        super("User Token Expired :(");
    }
}
