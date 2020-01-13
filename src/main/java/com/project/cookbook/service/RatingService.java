package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.PointedActions;
import com.project.cookbook.exception.RecipeNotFoundException;
import com.project.cookbook.exception.UserNotFoundException;
import com.project.cookbook.model.Rating;
import com.project.cookbook.model.Recipe;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.RatingRepository;
import com.project.cookbook.repository.RecipeRepository;
import com.project.cookbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public void addRating(GeneratedModels.Rating ratingRequest) {
        User user = userRepository.findById(ratingRequest.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(ratingRequest.getAuthorId()));
        User recipeAuthor = userRepository.findById(ratingRequest.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(ratingRequest.getAuthorId()));
        Recipe recipe = recipeRepository.findById(ratingRequest.getRecipeId())
                .orElseThrow(() -> new RecipeNotFoundException(ratingRequest.getRecipeId()));

        Rating rating = Rating.builder()
                .author(user)
                .rate(ratingRequest.getRate())
                .comment(ratingRequest.getComment())
                .recipe(recipe)
                .build();
        if (!ratingRequest.getComment().isEmpty()) {
            user.addPoints(PointedActions.ADD_COMMENT.getPoints());
            recipeAuthor.addPoints(PointedActions.GET_COMMENT.getPoints());
        } else {
            user.addPoints(PointedActions.ADD_RATING.getPoints());
            recipeAuthor.addPoints(PointedActions.GET_RATING.getPoints());
        }
        ratingRepository.saveAndFlush(rating);
        userRepository.saveAll(Arrays.asList(user, recipeAuthor));
        userRepository.flush();
    }
}
