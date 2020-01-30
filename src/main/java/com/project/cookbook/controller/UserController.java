package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.model.PrincipalUser;
import com.project.cookbook.security.CurrentUserAttribute;
import com.project.cookbook.service.UserService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/user/{id}")
    public String getUserProfile(@PathVariable long id) {
        GeneratedModels.UserSchema userProfile = userService.getUserProfile(id);
        try {
            return InOutService.write(userProfile);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping(value = "/user/passwordChange")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void changeUserPassword(@CurrentUserAttribute PrincipalUser principalUser,
                                   @RequestParam String newPassword) {
        userService.changePassword(principalUser, newPassword);
    }

    @DeleteMapping(value = "/user")
    @Secured("ROLE_USER")
    public void deleteUser(@CurrentUserAttribute PrincipalUser principalUser) {
        userService.deleteUserById(principalUser);
    }
}
