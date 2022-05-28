package com.authentication.role.permission.based.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownException extends RuntimeException {
    public UnknownException(String message) {
        super(message);
    }
}
