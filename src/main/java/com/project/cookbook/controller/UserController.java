package com.project.cookbook.controller;

import com.project.cookbook.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.BadAttributeValueExpException;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(name = "/user")
    public void createUser(@RequestParam String username, @RequestParam String email) {
        try {
            userService.createUser(username, email);
        } catch (BadAttributeValueExpException e) {
            e.printStackTrace();
        }
    }
}
