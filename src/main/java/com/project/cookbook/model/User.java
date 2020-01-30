package com.project.cookbook.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USERS")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_ID", nullable = false)
    private long id;

    @Column(name = "USR_USERNAME", nullable = false)
    private String username;

    @Column(name = "USR_EMAIL", nullable = false)
    private String email;

    @Column(name = "USR_PASSWORD", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "USR_PERMISSION_LEVEL", referencedColumnName = "PRM_ID")
    private PermissionLevel permissionLevel;

    @Column(name = "USR_POINTS")
    private long points;

    @OneToMany(mappedBy = "author")
    private List<Recipe> ownedRecipes;

    @OneToMany(mappedBy = "author")
    private List<Rating> ratings;

    @OneToOne(mappedBy = "user")
    private Book book;

    public void addPoints(long points) {
        this.points += points;
    }
}
