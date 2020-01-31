package com.project.cookbook.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "RECIPES")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RCP_ID", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RCP_USER_ID", referencedColumnName = "USR_ID")
    private User author;

    @Column(name = "RCP_TITLE", nullable = false)
    private String name;

    @Column(name = "RCP_TEXT")
    private String text;

    @Column(name = "RCP_IMAGE_URL")
    private String imageUrl;

    @ManyToMany(mappedBy = "recipes")
    private List<Book> books;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> ingredients;
}
