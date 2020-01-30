package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.model.PrincipalUser;
import com.project.cookbook.security.CurrentUserAttribute;
import com.project.cookbook.service.RecipeService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping(value = "/recipe")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void addNewRecipe(@CurrentUserAttribute PrincipalUser user,
                             @RequestBody String json) {
        try {
            GeneratedModels.RecipeSchema recipeRequest = InOutService.readMessageForType(json, GeneratedModels.RecipeSchema.getDefaultInstance());
            recipeService.createRecipe(recipeRequest, user);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/recipe/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getRecipe(@PathVariable long id) {
        GeneratedModels.RecipeSchema recipeSchema = recipeService.getRecipeById(id);
        try {
            return InOutService.write(recipeSchema);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping(value = "/recipe/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteRecipe(@CurrentUserAttribute PrincipalUser user,
                             @PathVariable long id) {
        recipeService.deleteRecipe(user, id);
    }

    @PutMapping(value = "/recipe/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void editRecipe(@PathVariable long id, @RequestBody String json, @CurrentUserAttribute PrincipalUser user) {
        try {
            GeneratedModels.RecipeSchema recipeRequest = InOutService.readMessageForType(json, GeneratedModels.RecipeSchema.getDefaultInstance());
            recipeService.editRecipe(id, recipeRequest, user);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/recipe")
    public String getFilteredRecipes(String namePattern,
                                     IngredientType mealType,
                                     List<GeneratedModels.Ingredient> ingredients,
                                     boolean getAllIngredientsMatches) throws InvalidProtocolBufferException {
        return InOutService.write(recipeService.getRecipes(namePattern, mealType, ingredients, getAllIngredientsMatches));
    }
}
