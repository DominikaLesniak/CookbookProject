package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.converter.IngredientConverter;
import com.project.cookbook.exception.IngredientNotFoundException;
import com.project.cookbook.model.Ingredient;
import com.project.cookbook.repository.IngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public GeneratedModels.Ingredient getIngredientById(long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        return ingredientOptional.map(IngredientConverter::convert).orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public GeneratedModels.IngredientsResponse getIngredientsStartingWith(String pattern) {
        List<GeneratedModels.Ingredient> ingredients = ingredientRepository.findAllByNameStartingWith(pattern).stream()
                .map(IngredientConverter::convert)
                .collect(Collectors.toList());
        return GeneratedModels.IngredientsResponse.newBuilder()
                .addAllIngredients(ingredients)
                .build();
    }

    public void deleteIngredient(long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        if (ingredientOptional.isPresent()) {
            ingredientRepository.delete(ingredientOptional.get());
        } else
            throw new IngredientNotFoundException(id);
    }

    public GeneratedModels.Ingredient editIngredient(long id, String name, IngredientType ingredientType) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);

        Ingredient editedIngredient = ingredientOptional.map(ingredient -> putNewValues(ingredient, name, ingredientType))
                .orElseThrow(() -> new IngredientNotFoundException(id));
        ingredientRepository.saveAndFlush(editedIngredient);
        return IngredientConverter.convert(editedIngredient);
    }

    Ingredient getIngredientByName(String name) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findAll().stream()
                .filter(ingredient -> ingredient.getName().equals(name))
                .findFirst();
        return ingredientOptional
                .orElseThrow(() -> new IngredientNotFoundException(name));
    }

    private Ingredient putNewValues(Ingredient ingredient, String name, IngredientType ingredientType) {
        if (name != null) {
            ingredient.setName(name);
        }
        if (ingredientType != null) {
            ingredient.setType(ingredientType.name());
        }
        return ingredient;
    }
}
