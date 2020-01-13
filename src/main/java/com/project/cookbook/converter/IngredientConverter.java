package com.project.cookbook.converter;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.model.Ingredient;

public class IngredientConverter {
    public static GeneratedModels.Ingredient convert(Ingredient ingredient) {
        return GeneratedModels.Ingredient.newBuilder()
                .setName(ingredient.getName())
                .setType(ingredient.getType())
                .build();
    }
}
