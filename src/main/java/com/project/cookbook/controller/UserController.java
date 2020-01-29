package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.service.UserService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/user")
    public void createUser(@RequestParam String username, @RequestParam String email) {
        try {
            userService.createUser(username, email);
        } catch (BadAttributeValueExpException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "user/{id}")
    public String getUserProfile(@PathVariable long id) {
        GeneratedModels.UserSchema userProfile = userService.getUserProfile(id);
        try {
            return InOutService.write(userProfile);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping(value = "user/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
    }
}
