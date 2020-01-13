package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.service.IngredientService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @PostMapping(value = "ingredient")
    public void addIngredient(@RequestParam String name,
                              @RequestParam IngredientType ingredientType) {
        ingredientService.addIngredient(name, ingredientType);
    }

    @PostMapping(value = "ingredient/body")
    public void addIngredientWithBody(@RequestBody String json) {
        try {
            GeneratedModels.Ingredient ingredient = InOutService.readMessageForType(json, GeneratedModels.Ingredient.getDefaultInstance());
            ingredientService.addIngredient(ingredient.getName(), IngredientType.valueOf(ingredient.getType()));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "ingredient/{id}")
    public String getIngredient(@PathVariable long id) throws InvalidProtocolBufferException {
        GeneratedModels.Ingredient ingredient = ingredientService.getIngredientById(id);

        return InOutService.write(ingredient);
    }

    @GetMapping(value = "ingredient")
    public String getIngredientsByName(@RequestParam String pattern) {
        try {
            GeneratedModels.IngredientsResponse ingredientsResponse = ingredientService.getIngredientsStartingWith(pattern);
            return InOutService.write(ingredientsResponse);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping(value = "ingredient/{id}")
    public void deleteIngredient(@PathVariable long id) {
        ingredientService.deleteIngredient(id);
    }

    @PutMapping(value = "ingredient/{id}")
    public String editIngredient(@PathVariable long id,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) IngredientType ingredientType) {
        try {
            GeneratedModels.Ingredient ingredient = ingredientService.editIngredient(id, name, ingredientType);
            return InOutService.write(ingredient);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }
}
