package com.server.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String credential) {
        super("User could not be found with credential:" + credential );
    }
}
