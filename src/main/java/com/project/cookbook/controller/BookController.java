package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.model.PrincipalUser;
import com.project.cookbook.security.CurrentUserAttribute;
import com.project.cookbook.service.BookService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping(value = "/book/{recipeId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void addRecipeToBook(@CurrentUserAttribute PrincipalUser principalUser, @PathVariable long recipeId) {
        bookService.addRecipeToBook(principalUser, recipeId);
    }

    @DeleteMapping(value = "/book/{recipeId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void removeRecipeFromBook(@CurrentUserAttribute PrincipalUser principalUser, @PathVariable long recipeId) {
        bookService.removeRecipeFromBook(principalUser, recipeId);
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
