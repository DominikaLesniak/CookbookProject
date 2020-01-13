package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.converter.RecipeConverter;
import com.project.cookbook.exception.RecipeNotFoundException;
import com.project.cookbook.model.Book;
import com.project.cookbook.model.Recipe;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.BookRepository;
import com.project.cookbook.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public void addRecipeToBook(long userId, long recipeId) {
        User user = userService.getUserById(userId);
        Book book = user.getBook();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        book.addRecipe(recipe);
        bookRepository.saveAndFlush(book);
    }

    public void removeRecipeFromBook(long userId, long recipeId) {
        User user = userService.getUserById(userId);
        Book book = bookRepository.findBookByUserEquals(user).get(0);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        book.removeRecipe(recipe);
        bookRepository.saveAndFlush(book);
    }

    public GeneratedModels.RecipeResponse getRecipesFromBook(long userId) {
        User user = userService.getUserById(userId);
        List<Recipe> recipes = user.getBook().getRecipes();

        List<GeneratedModels.RecipeSchema> recipeSchemas = recipes.stream()
                .map(RecipeConverter::convert)
                .collect(Collectors.toList());

        return GeneratedModels.RecipeResponse.newBuilder()
                .addAllRecipes(recipeSchemas)
                .build();
    }
}
