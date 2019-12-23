package com.project.cookbook.service;

import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.exception.IngredientNotFoundException;
import com.project.cookbook.model.Ingredient;
import com.project.cookbook.repository.IngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public void addIngredient(String name, IngredientType ingredientType) {
        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .type(ingredientType.name())
                .build();
        ingredientRepository.save(ingredient);
    }

    public Ingredient getIngredientById(long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        return ingredientOptional.orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public void deleteIngredient(long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        if (ingredientOptional.isPresent()) {
            ingredientRepository.delete(ingredientOptional.get());
        } else
            throw new IngredientNotFoundException(id);
    }

    public Ingredient editIngredient(long id, String name, IngredientType ingredientType) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);

        return ingredientOptional.map(ingredient -> putNewValues(ingredient, name, ingredientType))
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    Ingredient getIngredientByName(String name) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findAll().stream()
                .filter(ingredient -> ingredient.getName().equals(name))
                .findFirst();
        return ingredientOptional.orElse(null);
    }

    private Ingredient putNewValues(Ingredient ingredient, String name, IngredientType ingredientType) {
        if (!name.isBlank()) {
            ingredient.setName(name);
        }
        if (ingredientType != null) {
            ingredient.setType(ingredientType.name());
        }
        return ingredient;
    }
}
