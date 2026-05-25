package com.example.CampusMarket.controller;

import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ProductService productService;

    /**
     * 1. 메인 대문 경로 (http://localhost:8080/)
     * 로그인 안 한 사람은 로그인창으로 튕겨냅니다.
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            return "redirect:/main";
        }
        return "redirect:/user/login";
    }

    /**
     * 2. 진짜 상품 목록 메인 화면 경로 (http://localhost:8080/main)
     */
    @GetMapping("/main")
    public String mainPage(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            HttpServletRequest request,
            Model model
    ) {
        // [작업 1] 🔐 세션에서 로그인한 유저 정보를 꺼내 상단 헤더에 전달
        HttpSession session = request.getSession(false);
        if (session != null) {
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }

        // [작업 2] 📦 진짜 DB 상품 엔티티 리스트를 'findAll()'로 싹 긁어옵니다.
        List<Product> products = productService.findAll();

        // [작업 3] 🎯 index.mustache 주머니에 진짜 상품 데이터를 완벽하게 꽂아줍니다.
        model.addAttribute("products", products);

        // 필터링 상태 유지용 데이터
        model.addAttribute("category", category);
        model.addAttribute("status", status);

        return "index"; // templates/index.mustache 가동!
    }

    /**
     * 3. 상품 상세 페이지 조회
     * ⚙️ [최종 조치 완료] 하이픈 버그와 404/400 뺑뺑이 에러를 완벽히 해결하는 정석 경로 조준!
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        // 상세 페이지에서 꺼내 쓸 수 있도록 상품 고유 ID를 담아줍니다.
        model.addAttribute("id", id);

        // 🎯 templates/product/product_detail.mustache 위치를 정확히 가리키도록 수정했습니다.
        return "product/product_detail";
    }
}