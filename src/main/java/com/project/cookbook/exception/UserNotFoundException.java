package com.project.cookbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super("user with id " + id + " not found");
    }

    public UserNotFoundException(String usernameOrEmail) {
        super("user " + usernameOrEmail + " not found");
    }
}