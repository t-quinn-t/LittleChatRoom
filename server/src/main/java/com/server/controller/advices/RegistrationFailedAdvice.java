package com.server.controller.advices;

import com.server.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-19 9:27 p.m.
 */
@ControllerAdvice
public class RegistrationFailedAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserNotFound(UserAlreadyExistsException e) {
        return e.getLocalizedMessage();
    }
}
