package com.project.cookbook.controller;

import com.project.cookbook.model.User;
import com.project.cookbook.service.UserService;
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
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }
}
