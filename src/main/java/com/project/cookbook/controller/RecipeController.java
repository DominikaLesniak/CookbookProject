package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.service.RecipeService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping(value = "recipe")
    public void addNewRecipe(@RequestBody String json) {
        try {
            GeneratedModels.RecipeSchema recipeRequest = InOutService.readMessageForType(json, GeneratedModels.RecipeSchema.getDefaultInstance());
            recipeService.createRecipe(recipeRequest);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "recipe/{id}")
    public String getRecipe(@PathVariable long id) {
        GeneratedModels.RecipeSchema recipeSchema = recipeService.getRecipeById(id);
        try {
            return InOutService.write(recipeSchema);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping(value = "recipe/{id}")
    public void deleteRecipe(@PathVariable long id) {
        recipeService.deleteRecipe(id);
    }

    @PutMapping(value = "recipe/{id}")
    public void editRecipe(@PathVariable long id, @RequestBody String json) {
        try {
            GeneratedModels.RecipeSchema recipeRequest = InOutService.readMessageForType(json, GeneratedModels.RecipeSchema.getDefaultInstance());
            recipeService.editRecipe(id, recipeRequest);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
