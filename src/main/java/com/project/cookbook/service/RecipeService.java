package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.IngredientType;
import com.project.cookbook.constants.MealType;
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

import static java.util.Map.Entry.comparingByValue;
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

    public GeneratedModels.RecipeResponse getRecipes(GeneratedModels.RecipeFilters recipeFilters) {
        List<Recipe> collect = recipeRepository.findAll()
                .stream()
                .filter(filterByName(recipeFilters.getNamePattern()))
                .filter(filterByMealType(recipeFilters.getMealType()))
                .collect(toList());
        List<GeneratedModels.RecipeSchema> recipes = filterByIngredients(collect, getSafeList(recipeFilters), recipeFilters.getGetAllIngredientsMatches());
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
        return recipe -> namePattern.isBlank() || recipe.getName().toLowerCase()
                .contains(namePattern.toLowerCase());
    }

    private Predicate<Recipe> filterByMealType(String mealType) {
        return recipe -> mealType.isBlank() || recipe.getIngredients().stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getType())
                .filter(s -> !s.isBlank())
                .map(IngredientType::valueOf)
                .allMatch(matchMealType(MealType.valueOf(mealType)));
    }

    private List<GeneratedModels.RecipeSchema> filterByIngredients(List<Recipe> recipes,
                                                                   List<GeneratedModels.Ingredient> ingredients,
                                                                   boolean getAllIngredientsMatches) {
        if (ingredients.isEmpty()) {
            return recipes.stream()
                    .map(RecipeConverter::convert)
                    .collect(toList());
        }
        Map<Recipe, Long> sortedRecipes = new HashMap<>();
        for (Recipe recipe : recipes) {
            long matches = matchesMapper(recipe, ingredients);
            sortedRecipes.put(recipe, matches);
        }
        return sortedRecipes.entrySet()
                .stream()
                .sorted(comparingByValue())
                .filter(entry -> getAllIngredientsMatches ? entry.getValue().intValue() == ingredients.size() : true)
                .map(Map.Entry::getKey)
                .map(RecipeConverter::convert)
                .collect(toList());
    }

    private Predicate<IngredientType> matchMealType(MealType mealType) {
        return ingredientType -> {
            if (mealType.equals(MealType.REGULAR))
                return ingredientType.isRegular;
            else if (mealType.equals(MealType.VEGETARIAN))
                return ingredientType.isVegetarian;
            else
                return ingredientType.isVegan;
        };
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

    private List<GeneratedModels.Ingredient> getSafeList(GeneratedModels.RecipeFilters recipeFilters) {
        return recipeFilters.getIngredientsList() == null ? Collections.emptyList() : recipeFilters.getIngredientsList();
    }
}