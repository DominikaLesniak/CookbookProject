package com.project.cookbook.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class RecipesIngredientId implements Serializable {
    private long ingredient;
    private long recipe;
}
