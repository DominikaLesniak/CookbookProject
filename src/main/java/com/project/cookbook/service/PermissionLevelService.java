package com.project.cookbook.service;

import com.project.cookbook.constants.UserType;
import com.project.cookbook.model.PermissionLevel;
import com.project.cookbook.repository.PermissionLevelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PermissionLevelService {
    private final PermissionLevelRepository permissionLevelRepository;

    public void addPermissionLevel(UserType userType) {
        PermissionLevel permissionLevel = new PermissionLevel(userType);
        permissionLevelRepository.saveAndFlush(permissionLevel);
    }
}
