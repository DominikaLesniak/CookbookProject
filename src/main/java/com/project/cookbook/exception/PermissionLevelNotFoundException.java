package com.project.cookbook.exception;

import com.project.cookbook.constants.UserType;

public class PermissionLevelNotFoundException extends RuntimeException {
    public PermissionLevelNotFoundException(UserType userType) {
        super("Permission level not found for userType " + userType.name());
    }
}
