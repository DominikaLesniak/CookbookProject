package com.project.cookbook.exception;

import com.project.cookbook.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(User user) {
        super("User " + user.getUsername() + " inserted Incorrect password");
    }
}
