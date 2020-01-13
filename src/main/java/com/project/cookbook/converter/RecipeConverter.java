package com.project.cookbook.converter;

import com.project.cookbook.GeneratedModels.RecipeSchema;
import com.project.cookbook.model.Ingredient;
import com.project.cookbook.model.Recipe;
import com.project.cookbook.model.RecipeIngredient;

import static java.util.stream.Collectors.toList;

public class RecipeConverter {
    public static RecipeSchema convert(Recipe recipe) {
        RecipeSchema.Builder builder = RecipeSchema.newBuilder()
                .setName(recipe.getName())
                .setAuthorId(recipe.getAuthor().getId())
                .setText(recipe.getText())
                .addAllIngredients(recipe.getIngredients()
                        .stream()
                        .map(RecipeConverter::convertIngredients)
                        .collect(toList()));
        if (recipe.getImageUrl() != null) {
            builder.setImageUrl(recipe.getImageUrl());
        }
        return builder.build();
    }

    private static RecipeSchema.Ingredient convertIngredients(RecipeIngredient recipeIngredient) {
        Ingredient ingredient = recipeIngredient.getIngredient();
        return RecipeSchema.Ingredient.newBuilder()
                .setName(ingredient.getName())
                .setAmount(recipeIngredient.getAmount())
                .build();
    }
}
