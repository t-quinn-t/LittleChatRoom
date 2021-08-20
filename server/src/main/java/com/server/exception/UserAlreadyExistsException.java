package com.server.exception;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-19 9:25 p.m.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String cause) {
        super("User with this " + cause + " already exists in the system.");
    }
}
