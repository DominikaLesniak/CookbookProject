package com.project.cookbook.model;

import com.project.cookbook.constants.IngredientType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "INGREDIENTS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ING_ID", nullable = false)
    private long id;

    @Column(name = "ING_NAME", nullable = false)
    private String name;

    @Column(name = "ING_TYPE")
    private IngredientType type;

    @OneToMany(mappedBy = "ingredient")
    private Set<RecipeIngredient> recipes;
}
