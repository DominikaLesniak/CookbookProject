package com.project.cookbook.converter;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.model.User;

public class UserConverter {
    public static GeneratedModels.UserSchema convert(User user) {
        return GeneratedModels.UserSchema.newBuilder()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPoints(user.getPoints())
                .setOwnedRecipeNumber(user.getOwnedRecipes().size())
                .setRatingsNumber(user.getRatings().size())
                .build();
    }
}
