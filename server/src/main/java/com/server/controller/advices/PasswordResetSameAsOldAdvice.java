package com.server.controller.advices;

import com.server.exception.PasswordSameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-07-28 10:05 p.m.
 */
@ControllerAdvice
public class PasswordResetSameAsOldAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PasswordSameException.class)
    public String passwordResetSameAsOld(PasswordSameException e) {
        return e.getMessage();
    }
}
