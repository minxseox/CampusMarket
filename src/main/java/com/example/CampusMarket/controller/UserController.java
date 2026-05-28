package com.example.CampusMarket.controller;

import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.dto.UserForm;
import com.example.CampusMarket.repository.ProductRepository;
import com.example.CampusMarket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ProductRepository productRepository;

    @GetMapping("/signup")
    public String signup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            return "redirect:/main";
        }
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(UserForm form, RedirectAttributes rttr) {
        // 1. 서비스 계층에 중복 검사를 요청합니다.
        String errorMessage = userService.checkDuplicate(form);

        // 2. 만약 에러 메시지가 반환되었다면? (중복 발생)
        if (errorMessage != null) {
            // 8장에서 배운 addFlashAttribute를 이용해 에러 메시지를 싣고 리다이렉트!
            rttr.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/user/signup";
        }

        // 3. 문제가 없으면 정상적으로 가입 진행
        userService.create(form);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            return "redirect:/main";
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

    @GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }

        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

        model.addAttribute("nickname", loginUser.getNickname());
        model.addAttribute("email", loginUser.getEmail());

        if (loginUser.getCreatedDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            model.addAttribute("createdDate", loginUser.getCreatedDate().format(formatter));
        } else {
            model.addAttribute("createdDate", "정보 없음");
        }

        List<Product> products = productRepository.findByAuthor(loginUser);
        model.addAttribute("products", products);

        return "user/mypage";
    }

    @GetMapping("/mypage/edit")
    public String editMypage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }

        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
        model.addAttribute("nickname", loginUser.getNickname());
        model.addAttribute("email", loginUser.getEmail());

        return "user/mypage_edit";
    }

    @PostMapping("/mypage/edit")
    public String updateMypage(String email, String nickname, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }

        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
        SiteUser updatedUser = userService.updateUserInfo(loginUser.getId(), email, nickname);
        session.setAttribute("loginUser", updatedUser);

        return "redirect:/user/mypage";
    }
}