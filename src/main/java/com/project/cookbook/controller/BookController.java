package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.service.BookService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping(value = "{userId}/book/{recipeId}")
    public void addRecipeToBook(@PathVariable long userId, @PathVariable long recipeId) {
        bookService.addRecipeToBook(userId, recipeId);
    }

    @DeleteMapping(value = "{userId}/book/{recipeId}")
    public void removeRecipeFromBook(@PathVariable long userId, @PathVariable long recipeId) {
        bookService.removeRecipeFromBook(userId, recipeId);
    }

    @GetMapping(value = "{userId}/book")
    public String getRecipesFromBook(@PathVariable long userId) {
        try {
            GeneratedModels.RecipeResponse recipeResponse = bookService.getRecipesFromBook(userId);
            return InOutService.write(recipeResponse);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }
}
