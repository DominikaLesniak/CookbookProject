package com.project.cookbook.service;

import com.project.cookbook.model.Ingredient;
import com.project.cookbook.model.Recipe;
import com.project.cookbook.model.RecipeIngredient;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.RecipeIngredientRepository;
import com.project.cookbook.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@AllArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientService ingredientService;

    public void createRecipe(User user, String name, String text, Map<String, String> ingredients) {
        Recipe recipe = Recipe.builder()
                .books(Collections.singletonList(user.getBook()))
                .name(name)
                .text(text)
                .owner(user)
                .build();
        Recipe save = recipeRepository.save(recipe);
        ingredients.forEach((ingredientName, amount) -> {
            Ingredient ingredientByName = ingredientService.getIngredientByName(ingredientName);
            RecipeIngredient recipeIngredient = new RecipeIngredient(ingredientByName, save, amount);
            recipeIngredientRepository.save(recipeIngredient);
        });
        recipeRepository.flush();
        recipeIngredientRepository.flush();
    }
}
