package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.PointedActions;
import com.project.cookbook.converter.RecipeConverter;
import com.project.cookbook.exception.RecipeNotFoundException;
import com.project.cookbook.model.*;
import com.project.cookbook.repository.BookRepository;
import com.project.cookbook.repository.RecipeIngredientRepository;
import com.project.cookbook.repository.RecipeRepository;
import com.project.cookbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientService ingredientService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void createRecipe(GeneratedModels.RecipeSchema recipeRequest) {
        User user = userService.getUserById(recipeRequest.getAuthorId());


        Recipe recipe = Recipe.builder()
                .name(recipeRequest.getName())
                .text(recipeRequest.getText())
                .author(user)
                .imageUrl(recipeRequest.getImageUrl())
                .build();
        Recipe savedRecipe = recipeRepository.save(recipe);
        recipeRequest.getIngredientsList().forEach(ingredient -> {
            Ingredient ingredientByName = ingredientService.getIngredientByName(ingredient.getName());
            RecipeIngredient recipeIngredient = new RecipeIngredient(ingredientByName, savedRecipe, ingredient.getAmount());
            recipeIngredientRepository.save(recipeIngredient);
        });
        recipeRepository.flush();
        recipeIngredientRepository.flush();

        user.addPoints(PointedActions.ADD_RECIPE.getPoints());
        userRepository.saveAndFlush(user);

        Book book = user.getBook();
        book.addRecipe(savedRecipe);
        bookRepository.saveAndFlush(book);
    }

    public GeneratedModels.RecipeSchema getRecipeById(long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        return recipeOptional.map(RecipeConverter::convert)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    public void deleteRecipe(long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        recipeOptional.ifPresent(recipeRepository::delete);
    }

    public void editRecipe(long id, GeneratedModels.RecipeSchema recipeSchema) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        recipeOptional.map(recipe -> putNewValues(recipe, recipeSchema))
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    private Recipe putNewValues(Recipe recipe, GeneratedModels.RecipeSchema recipeSchema) {
        if (!recipeSchema.getName().isEmpty()) {
            recipe.setName(recipeSchema.getName());
        }
        if (!recipeSchema.getText().isEmpty()) {
            recipe.setText(recipeSchema.getText());
        }
        return recipe;
    }
}
