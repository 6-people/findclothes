package com.people.findclothes.controller;

import com.people.findclothes.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

}