package com.server.controller.advices;

import com.server.exception.CredentialFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CredentialFailureAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CredentialFailureException.class)
    public String loginFailed(CredentialFailureException e) {
        return e.getMessage();
    }
}
