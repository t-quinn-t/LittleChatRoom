package com.server.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String credential) {
        super(credential.equals("unknown") ?
                "Oops, seems like you are disconnected!" :
                "User could not be found with given information:" + credential);
    }

}
