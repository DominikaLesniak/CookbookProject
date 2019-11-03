package com.project.cookbook.service;

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
        Optional<Ingredient> ingredient = ingredientRepository.findAll().stream()
                .filter(ingr -> ingr.getName().equals(name))
                .findFirst();
        return ingredient.orElse(null);
    }
}
