package com.example.CampusMarket.controller;

import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.dto.UserForm;
import com.example.CampusMarket.repository.ProductRepository;
import com.example.CampusMarket.service.UserService;
import com.example.CampusMarket.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        // 헤더 에러 방지를 위해 빈 keyword 추가
        model.addAttribute("keyword", "");

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
        // 헤더 에러 방지를 위해 빈 keyword 추가
        model.addAttribute("keyword", "");

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

    /**
     * 마이페이지: 회원 정보 및 내가 올린 상품 리스트 조회
     */
    @GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }

        // 헤더 에러 방지를 위해 빈 keyword 추가
        model.addAttribute("keyword", "");

        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

        // 1. 유저 정보 설정
        model.addAttribute("nickname", loginUser.getNickname());
        model.addAttribute("email", loginUser.getEmail());

        if (loginUser.getCreatedDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            model.addAttribute("createdDate", loginUser.getCreatedDate().format(formatter));
        } else {
            model.addAttribute("createdDate", "정보 없음");
        }

        // 2. 내가 올린 상품 조회
        List<Product> products = productRepository.findByAuthor(loginUser);
        model.addAttribute("products", products);

        return "user/mypage";
    }
}