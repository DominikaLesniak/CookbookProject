package com.project.cookbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(long id) {
        super("ingredient with id " + id + " not found");
    }

    public IngredientNotFoundException(String name) {
        super("ingredient with name " + name + " not found");
    }
}
