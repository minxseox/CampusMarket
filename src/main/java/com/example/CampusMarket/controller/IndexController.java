package com.example.CampusMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    /**
     * 🌟 1. 메인 대문 경로 (http://localhost:8080/)
     * 사용자가 접속하자마자 팀원이 만든 로그인 화면으로 강제 이동(Redirect) 시킵니다!
     */
    @GetMapping("/")
    public String index() {
        // 팀원이 만든 유저 로그인 페이지 주소인 "/user/login"으로 튕겨버립니다.
        // ⚠️ 만약 팀원이 로그인 페이지 주소를 다르게 만들었다면 (예: "/login") 여기 문자열만 바꿔주세요!
        return "redirect:/user/login";
    }

    /**
     * 💡 기존 메인 상품 목록 화면은 주소를 분리해두면
     * 나중에 로그인 성공한 후에 이 주소로 리다이렉트 시켜서 재활용할 수 있습니다!
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