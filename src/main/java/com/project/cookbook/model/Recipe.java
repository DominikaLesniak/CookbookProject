package com.project.cookbook.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "RECIPES")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RCP_ID", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RCP_USER_ID", referencedColumnName = "USR_ID")
    private User owner;

    @Column(name = "RCP_TITLE")
    private String name;

    @Column(name = "RCP_TEXT")
    private String text;

    @ManyToMany(mappedBy = "recipes")
    private List<Book> books;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> ingredients;
}
