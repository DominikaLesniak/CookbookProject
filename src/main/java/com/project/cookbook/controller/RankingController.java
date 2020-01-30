package com.project.cookbook.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.project.cookbook.service.UserService;
import com.project.cookbook.utils.InOutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RankingController {
    private final UserService userService;

    @GetMapping("/ranking/users")
    public String getTopUsers(@RequestParam(required = false, defaultValue = "15") int resultListSize) throws InvalidProtocolBufferException {
        return InOutService.write(userService.getTopUsers(resultListSize));
    }
}
