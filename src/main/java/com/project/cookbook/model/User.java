package com.project.cookbook.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USERS")
@Setter
@Getter
@Builder
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_ID", nullable = false)
    private long id;

    @Column(name = "USR_USERNAME")
    private String username;

    @Column(name = "USR_EMAIL")
    private String email;

    @Column(name = "USR_POINTS")
    private long points;

    @OneToMany(mappedBy = "owner")
    private List<Recipe> ownedRecipes;

    @OneToOne(mappedBy = "user")
    private Book book;
}
