package com.project.cookbook.controller;

import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.model.Ingredient;
import com.project.cookbook.service.IngredientService;
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

    @GetMapping(value = "ingredient/{id}")
    public Ingredient getIngredient(@PathVariable long id) {
        return ingredientService.getIngredientById(id);
    }

    @DeleteMapping(value = "ingredient/{id}")
    public void deleteIngredient(@PathVariable long id) {
        ingredientService.deleteIngredient(id);
    }

    @PutMapping(value = "ingredient/{id}")
    public Ingredient editIngredient(@PathVariable long id,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) IngredientType ingredientType) {
        return ingredientService.editIngredient(id, name, ingredientType);
    }
}
