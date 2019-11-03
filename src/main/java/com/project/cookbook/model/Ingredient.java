package com.project.cookbook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "INGREDIENTS")
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ING_ID", nullable = false)
    private long id;

    @Column(name = "ING_NAME")
    private String name;

    @Column(name = "ING_TYPE")
    private String type;

    @OneToMany(mappedBy = "ingredient")
    private Set<RecipeIngredient> recipes;
}
