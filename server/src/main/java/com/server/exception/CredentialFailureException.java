package com.server.exception;

public class CredentialFailureException extends RuntimeException {

    public CredentialFailureException() {
        super("Something does not seems to be correct, Please Try Again");
    }
}
