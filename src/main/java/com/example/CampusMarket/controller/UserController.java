package com.example.CampusMarket.controller;

import com.example.CampusMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        userService.create(username, email, password);
        return "redirect:/user/login"; // 회원가입 완료 후 로그인 페이지로 유도
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
}