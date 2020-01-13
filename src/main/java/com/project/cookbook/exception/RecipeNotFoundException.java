package com.project.cookbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(long id) {
        super("recipe with id " + id + " not found");
    }
}
