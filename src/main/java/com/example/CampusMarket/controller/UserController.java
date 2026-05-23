package com.example.CampusMarket.controller;

import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.dto.UserForm;
import com.example.CampusMarket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(UserForm form) {
        userService.create(form);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }
        return "user/login";
    }

    @PostMapping("/login")
    public String login(UserForm form, HttpServletRequest request) {
        SiteUser loginUser = userService.login(form.getUsername(), form.getPassword());

        if (loginUser == null) {
            return "redirect:/user/login?error";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}