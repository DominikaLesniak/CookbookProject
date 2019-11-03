package com.project.cookbook.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "RECIPES_INGREDIENTS")
@IdClass(RecipesIngredientId.class)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredient {
    @Id
    @ManyToOne
    @JoinColumn(name = "RIN_INGREDIENT_ID", referencedColumnName = "ING_ID", nullable = false)
    private Ingredient ingredient;

    @Id
    @ManyToOne
    @JoinColumn(name = "RIN_RECIPE_ID", referencedColumnName = "RCP_ID", nullable = false)
    private Recipe recipe;

    @Column(name = "RIN_INGREDIENT_AMOUNT")
    private String amount;
}
