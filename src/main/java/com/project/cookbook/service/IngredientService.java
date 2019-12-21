package com.project.cookbook.service;

import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.model.Ingredient;
import com.project.cookbook.repository.IngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public Ingredient getIngredientByName(String name) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findAll().stream()
                .filter(ingredient -> ingredient.getName().equals(name))
                .findFirst();
        return ingredientOptional.orElse(null);
    }

    public void addIngredient(String name, IngredientType ingredientType) {
        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .type(ingredientType)
                .build();
        ingredientRepository.save(ingredient);
    }

    public Ingredient getIngredientById(long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        return ingredientOptional.orElse(null);
    }

    public void deleteIngredient(long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        ingredientOptional.ifPresent(ingredientRepository::delete);
    }
}
