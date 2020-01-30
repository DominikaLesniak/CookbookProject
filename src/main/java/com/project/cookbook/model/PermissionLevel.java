package com.project.cookbook.model;

import com.project.cookbook.constants.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PERMISSION_LEVELS")
@Setter
@Getter
@NoArgsConstructor
public class PermissionLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRM_ID", nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRM_PERMISSIONS")
    private UserType userType;

    @OneToMany(mappedBy = "permissionLevel")
    private List<User> users;

    public PermissionLevel(UserType userType) {
        this.userType = userType;
    }
}
