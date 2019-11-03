package com.project.cookbook.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RecipesIngredientId implements Serializable {
    private long ingredient;
    private long recipe;
}
