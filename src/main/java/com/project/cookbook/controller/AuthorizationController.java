package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.GeneratedModels;
import com.project.cookbook.service.AuthorizationService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/authorization")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/signIn")
    public ResponseEntity authenticateUser(@RequestBody String jsonRequest) throws InvalidProtocolBufferException {
        GeneratedModels.LoginRequest loginRequest = InOutService.readMessageForType(jsonRequest, GeneratedModels.LoginRequest.getDefaultInstance());

        return authorizationService.authenticateUser(loginRequest);
    }

    @PostMapping("/signUp")
    public ResponseEntity createUser(@RequestBody String jsonRequest) throws InvalidProtocolBufferException {
        GeneratedModels.SignUpRequest signUpRequest = InOutService.readMessageForType(jsonRequest, GeneratedModels.SignUpRequest.getDefaultInstance());

        return authorizationService.signUp(signUpRequest);
    }
}
