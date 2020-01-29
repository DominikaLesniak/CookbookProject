package com.project.cookbook.service;

import com.project.cookbook.GeneratedModels;
import com.project.cookbook.constants.PointedActions;
import com.project.cookbook.converter.RatingConverter;
import com.project.cookbook.exception.RatingNotFoundException;
import com.project.cookbook.exception.RecipeNotFoundException;
import com.project.cookbook.model.Rating;
import com.project.cookbook.model.Recipe;
import com.project.cookbook.model.User;
import com.project.cookbook.repository.RatingRepository;
import com.project.cookbook.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final RecipeRepository recipeRepository;

    public Rating getRating(long ratingId) {
        return ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RatingNotFoundException(ratingId));
    }

    public GeneratedModels.RatingsResponse getRecipeRatings(long recipeId) {
        Recipe ratedRecipe = getRatedRecipe(recipeId);
        List<Rating> ratings = ratingRepository.findAllByRecipeEquals(ratedRecipe);
        List<GeneratedModels.Rating> convertedRatings = ratings.stream()
                .map(RatingConverter::convert)
                .collect(Collectors.toList());

        return GeneratedModels.RatingsResponse.newBuilder()
                .setAverageRating(calculateAverageRate(ratings))
                .addAllRatings(convertedRatings)
                .build();
    }

    public void addRating(GeneratedModels.Rating ratingRequest) {
        User user = userService.getUserById(ratingRequest.getAuthorId());
        Recipe recipe = getRatedRecipe(ratingRequest.getRecipeId());
        User recipeAuthor = recipe.getAuthor();
        Rating rating = Rating.builder()
                .author(user)
                .rate(ratingRequest.getRate())
                .comment(ratingRequest.getComment())
                .recipe(recipe)
                .build();
        if (!ratingRequest.getComment().isEmpty()) {
            userService.updateUserPoints(user, PointedActions.ADD_COMMENT.getPoints());
            userService.updateUserPoints(recipeAuthor, PointedActions.GET_COMMENT.getPoints());
        } else {
            userService.updateUserPoints(user, PointedActions.ADD_RATING.getPoints());
            userService.updateUserPoints(recipeAuthor, PointedActions.GET_RATING.getPoints());
        }
        ratingRepository.saveAndFlush(rating);
    }

    public void editRating(long ratingId, GeneratedModels.Rating ratingRequest) {
        Rating rating = getRating(ratingId);
        insertNewValues(rating, ratingRequest);
        ratingRepository.saveAndFlush(rating);
    }

    public void deleteRating(long ratingId, long userId) {
        Rating rating = getRating(ratingId);
        if (rating.getAuthor().getId() == userId) {
            PointedActions actions = rating.getComment().isBlank() ? PointedActions.ADD_RATING : PointedActions.ADD_COMMENT;
            userService.updateUserPoints(userId, actions.getPoints());
            ratingRepository.delete(rating);
        }
    }

    private String calculateAverageRate(List<Rating> ratings) {
        OptionalDouble average = ratings.stream()
                .mapToInt(Rating::getRate)
                .average();
        return average.isPresent() ? String.valueOf(average.getAsDouble()) : "";
    }

    private void insertNewValues(Rating rating, GeneratedModels.Rating ratingRequest) {
        if (!ratingRequest.getComment().isEmpty()) {
            rating.setComment(ratingRequest.getComment());
        }
        if (ratingRequest.getRate() > 0) {
            rating.setRate(ratingRequest.getRate());
        }
    }

    private Recipe getRatedRecipe(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }
}
