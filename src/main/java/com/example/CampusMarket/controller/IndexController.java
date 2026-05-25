package com.example.CampusMarket.controller;

import com.example.CampusMarket.entity.SiteUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    /**
     * 🌟 1. 메인 대문 경로 (http://localhost:8080/)
     * 팀장님의 로그인 강제 이동 흐름 + 팀원이 추가한 세션 닉네임 로직을 하나로 합쳤습니다!
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        // 1. 현재 브라우저의 세션 장바구니를 확인합니다.
        HttpSession session = request.getSession(false);

        // 2. 세션이 존재하고, 그 안에 로그인 유저 정보가 들어있다면? -> 이미 로그인한 사람!
        if (session != null) {
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
            if (loginUser != null) {
                // 팀원이 header.mustache에 연동해둔 유저 닉네임을 화면 주머니에 쏙 넣어줍니다.
                model.addAttribute("nickname", loginUser.getNickname());
                return "index"; // 로그인했으니 튕기지 않고 바로 상품 목록 메인 화면 보여주기!
            }
        }

        // 3. 세션 장바구니가 비어있다면? -> 로그인 안 한 사람이므로 로그인 페이지로 강제 배송!
        return "redirect:/user/login";
    }

    /**
     * 💡 로그인 처리 완료 후 다이렉트로 상품 목록만 띄워주고 싶을 때 사용하는 전용 경로입니다.
     */
    @GetMapping("/main")
    public String mainPage(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            Model model
    ) {
        model.addAttribute("userName", "강냉이");
        model.addAttribute("category", category);
        model.addAttribute("status", status);

        return "index"; // templates/index.mustache로 이동
    }

    /**
     * 2. 상품 상세 페이지 조회
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "product/detail"; // templates/product/detail.mustache로 이동
    }
}