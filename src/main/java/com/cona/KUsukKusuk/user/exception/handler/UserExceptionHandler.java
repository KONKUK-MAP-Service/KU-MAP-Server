package com.cona.KUsukKusuk.user.exception.handler;

import com.cona.KUsukKusuk.global.response.ErrorResponse;
import com.cona.KUsukKusuk.global.response.HttpResponse;
import com.cona.KUsukKusuk.user.exception.PasswordNotMatchException;
import com.cona.KUsukKusuk.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HttpResponse<ErrorResponse> userNotFoundExceptionHandler(UserNotFoundException e) {
        return HttpResponse.status(e.getHttpStatus())
                .body(ErrorResponse.from(e.getHttpStatus(), e.getMessage()));
    }
    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public HttpResponse<ErrorResponse> passwordNotMatchExceptionHandler(PasswordNotMatchException e) {
        return HttpResponse.status(e.getHttpStatus())
                .body(ErrorResponse.from(e.getHttpStatus(), e.getMessage()));
    }
}
