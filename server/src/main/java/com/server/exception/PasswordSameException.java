package com.server.exception;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-07-28 9:58 p.m.
 */
public class PasswordSameException extends RuntimeException {
    public PasswordSameException() {
        super("New Password Cannot be the same as Old One");
    }
}
