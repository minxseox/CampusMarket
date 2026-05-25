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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ProductService productService;

    /**
     * 1. 메인 대문 경로 (http://localhost:8080/)
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
     * 3. [수정 완료] 상품 상세 페이지 조회
     * 중복 매핑 충돌을 방지하기 위해 숫자 정규식 패턴을 유지합니다.
     */
    @GetMapping("/product/{id:[0-9]+}")
    public String productDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        // 상세 창에서 뿌려줄 진짜 단건 데이터를 DB에서 가져옵니다.
        Product product = productService.findById(id);

        // 💡 [핵심 추가] 현재 로그인한 사람이 이 상품의 작성자인지 확인합니다.
        boolean isAuthor = false;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

            // 상품에 작성자가 존재하고, 로그인한 유저 닉네임과 작성자 닉네임이 같으면 본인!
            if (product.getAuthor() != null && loginUser.getNickname().equals(product.getAuthor().getNickname())) {
                isAuthor = true;
            }
        }

        // html 주머니에 변수 고리를 걸어 전달합니다.
        model.addAttribute("id", id);
        model.addAttribute("product", product);

        // 💡 템플릿 엔진이 알 수 있도록 isAuthor 값(true/false)을 담아줍니다.
        model.addAttribute("isAuthor", isAuthor);

        // templates/product/product_detail.mustache 가동!
        return "product/product_detail";
    }

    /**
     * 4. 상품 삭제 처리 API
     */
    @DeleteMapping("/api/product/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 세션 확인
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginUser") == null) {
                return ResponseEntity.status(401).body("로그인이 필요한 서비스입니다.");
            }

            // 로그인한 유저의 정보(SiteUser)를 꺼냅니다.
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

            // 서비스 단의 delete 메서드에 '상품 ID'와 '로그인한 유저의 닉네임'을 함께 던져줍니다.
            productService.delete(id, loginUser.getNickname());

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}