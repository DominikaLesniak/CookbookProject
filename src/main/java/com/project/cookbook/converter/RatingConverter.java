package com.project.cookbook.converter;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.model.Rating;

public class RatingConverter {
    public static GeneratedModels.Rating convert(Rating rating) {
        return GeneratedModels.Rating.newBuilder()
                .setRate(rating.getRate())
                .setComment(rating.getComment())
                .setAuthorId(rating.getAuthor().getId())
                .setRecipeId(rating.getRecipe().getId())
                .build();
    }
}
