package com.project.cookbook.repository;

import com.project.cookbook.model.Rating;
import com.project.cookbook.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByRecipeEquals(Recipe recipe);
}
