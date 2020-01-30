package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.model.PrincipalUser;
import com.project.cookbook.security.CurrentUserAttribute;
import com.project.cookbook.service.RatingService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("recipe/{recipeId}/rating")
    @Secured("ROLE_USER")
    public String getRatings(@PathVariable long recipeId) {
        try {
            GeneratedModels.RatingsResponse ratingsResponse = ratingService.getRecipeRatings(recipeId);
            return InOutService.write(ratingsResponse);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("recipe/{recipeId}/rating")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void addRating(@RequestBody String jsonRecipeRequest) {
        try {
            GeneratedModels.Rating ratingRequest = InOutService.readMessageForType(jsonRecipeRequest, GeneratedModels.Rating.getDefaultInstance());
            ratingService.addRating(ratingRequest);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("recipe/{recipeId}/rating/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void editRating(@PathVariable long id, @RequestBody String jsonRecipeRequest) {
        try {
            GeneratedModels.Rating ratingRequest = InOutService.readMessageForType(jsonRecipeRequest, GeneratedModels.Rating.getDefaultInstance());
            ratingService.editRating(id, ratingRequest);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("recipe/{recipeId}/rating/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void deleteRating(@PathVariable long id, @CurrentUserAttribute PrincipalUser user) {
        ratingService.deleteRating(id, user);
    }
}
