package com.project.cookbook.repository;

import com.project.cookbook.constants.UserType;
import com.project.cookbook.model.PermissionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionLevelRepository extends JpaRepository<PermissionLevel, Long> {
    public Optional<PermissionLevel> findByUserType(UserType userType);
}
