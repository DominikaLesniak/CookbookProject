package com.project.cookbook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "BOOKS")
@Setter
@Getter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BK_ID", nullable = false)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BK_USER_ID", referencedColumnName = "USR_ID")
    private User user;

    @ManyToMany
    @JoinTable(name = "BOOKS_RECIPES",
            joinColumns = @JoinColumn(name = "BR_BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "BR_RECIPE_ID"))
    private List<Recipe> recipes;
}
