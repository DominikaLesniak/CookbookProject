package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.service.RatingService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("recipe/{recipeId}/rating")
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
    public void addRating(@RequestBody String jsonRecipeRequest) {
        try {
            GeneratedModels.Rating ratingRequest = InOutService.readMessageForType(jsonRecipeRequest, GeneratedModels.Rating.getDefaultInstance());
            ratingService.addRating(ratingRequest);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("recipe/{recipeId}/rating/{id}")
    public void editRating(@PathVariable long id, @RequestBody String jsonRecipeRequest) {
        try {
            GeneratedModels.Rating ratingRequest = InOutService.readMessageForType(jsonRecipeRequest, GeneratedModels.Rating.getDefaultInstance());
            ratingService.editRating(id, ratingRequest);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("recipe/{recipeId}/rating/{id}")
    public void editRating(@PathVariable long id, @RequestParam long userId) {
        ratingService.deleteRating(id, userId);
    }
}
