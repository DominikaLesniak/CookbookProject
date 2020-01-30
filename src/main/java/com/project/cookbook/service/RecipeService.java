package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.constants.PointedActions;
import com.project.cookbook.converter.RecipeConverter;
import com.project.cookbook.exception.RecipeNotFoundException;
import com.project.cookbook.exception.WrongUserException;
import com.project.cookbook.model.*;
import com.project.cookbook.repository.BookRepository;
import com.project.cookbook.repository.RecipeIngredientRepository;
import com.project.cookbook.repository.RecipeRepository;
import com.project.cookbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientService ingredientService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void createRecipe(GeneratedModels.RecipeSchema recipeRequest, PrincipalUser principalUser) {
        User user = userService.getUserById(recipeRequest.getAuthorId());

        if (user.getId() != principalUser.getId()) {
            throw new WrongUserException(principalUser.getId(), user.getId());
        }

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

    public void deleteRecipe(PrincipalUser principalUser, long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            if (recipe.getAuthor().getId() != principalUser.getId())
                throw new WrongUserException(principalUser.getId(), recipe.getAuthor().getId());
            recipeRepository.delete(recipe);
        }
    }

    public void editRecipe(long id, GeneratedModels.RecipeSchema recipeSchema, PrincipalUser principalUser) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            if (recipe.getAuthor().getId() != principalUser.getId())
                throw new WrongUserException(principalUser.getId(), recipe.getAuthor().getId());
            Recipe editedRecipe = putNewValues(recipe, recipeSchema);
            recipeRepository.saveAndFlush(editedRecipe);
        }
    }

    public GeneratedModels.RecipeResponse getRecipes(String namePattern,
                                                     IngredientType mealType,
                                                     List<GeneratedModels.Ingredient> ingredients,
                                                     boolean getAllIngredientsMatches) {
        List<Recipe> collect = recipeRepository.findAll()
                .stream()
                .filter(filterByName(namePattern))
                .filter(filterByMealType(mealType))
                .collect(toList());
        List<GeneratedModels.RecipeSchema> recipes = filterByIngredients(collect, getSafeList(ingredients), getAllIngredientsMatches);
        return GeneratedModels.RecipeResponse.newBuilder()
                .addAllRecipes(recipes)
                .build();
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

    private Predicate<Recipe> filterByName(String namePattern) {
        return recipe -> namePattern == null || recipe.getName().toLowerCase()
                .contains(namePattern.toLowerCase());
    }

    private Predicate<Recipe> filterByMealType(IngredientType mealType) {
        return recipe -> mealType == null || recipe.getIngredients().stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getType())
                .filter(s -> !s.isBlank())
                .allMatch(ingredientType -> ingredientType.equals(mealType.name()));
    }

    private List<GeneratedModels.RecipeSchema> filterByIngredients(List<Recipe> recipes,
                                                                   List<GeneratedModels.Ingredient> ingredients,
                                                                   boolean getAllIngredientsMatches) {
        Map<Long, Recipe> sortedRecipes = new HashMap<>();
        for (Recipe recipe : recipes) {
            long matches = matchesMapper(recipe, ingredients);
            sortedRecipes.put(matches, recipe);
        }
        return sortedRecipes.entrySet()
                .stream()
                .sorted(comparingByKey())
                .filter(entry -> getAllIngredientsMatches ? entry.getKey().intValue() == ingredients.size() : true)
                .map(Map.Entry::getValue)
                .map(RecipeConverter::convert)
                .collect(toList());
    }

    private long matchesMapper(Recipe recipe, List<GeneratedModels.Ingredient> ingredients) {
        return recipe.getIngredients().stream()
                .map(RecipeIngredient::getIngredient)
                .filter(ingredient -> getMatch(ingredient, ingredients))
                .count();
    }

    private boolean getMatch(Ingredient ingredient, List<GeneratedModels.Ingredient> ingredients) {
        return ingredients.stream()
                .map(GeneratedModels.Ingredient::getName)
                .anyMatch(ingredient1 -> ingredient.getName().toLowerCase().equals(ingredient1.toLowerCase()));
    }

    private List<GeneratedModels.Ingredient> getSafeList(List<GeneratedModels.Ingredient> ingredients) {
        return ingredients == null ? Collections.emptyList() : ingredients;
    }
}