package com.server.controller.advices;

import com.server.exception.UserTokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-08 12:46 a.m.
 */

@ControllerAdvice
public class UserTokenExpiredAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserTokenExpiredException.class)
    public String userTokenExpired(UserTokenExpiredException e) {
        return e.getMessage();
    }
}
