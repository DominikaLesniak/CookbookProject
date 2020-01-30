package com.project.cookbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongUserException extends RuntimeException {
    public WrongUserException(long loggedUserId, long requestedUserId) {
        super("Logged user with id: " + loggedUserId + " tried to call method for user with id: " + requestedUserId);
    }
}
