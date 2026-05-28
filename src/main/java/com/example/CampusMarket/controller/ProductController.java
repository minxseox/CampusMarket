package com.example.CampusMarket.controller;

import com.example.CampusMarket.dto.ProductSaveRequestDto;
import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.ProductStatus;
import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.service.CommentService;
import com.example.CampusMarket.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final CommentService commentService;

    /**
     * 1. 상품 등록 페이지로 이동
     */
    @GetMapping("/product/save")
    public String productSave(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
            model.addAttribute("nickname", loginUser.getNickname());
        } else {
            return "redirect:/user/login";
        }
        model.addAttribute("keyword", "");
        return "product/product_save";
    }

    /**
     * 2. 상품 등록 실행 (API)
     */
    @PostMapping("/api/v1/product")
    @ResponseBody
    public String save(@Valid @RequestBody ProductSaveRequestDto requestDto,
                       BindingResult bindingResult,
                       HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "로그인이 필요한 서비스입니다.";
        }
        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

        productService.save(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getPrice(),
                requestDto.getCategory(),
                loginUser
        );

        return "success";
    }

    /**
     * 3. 상품 수정 페이지로 이동
     */
    @GetMapping("/product/{id}/edit")
    public String productEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/product_edit";
    }

    /**
     * 4. 상품 수정 실행 (API)
     */
    @PutMapping("/api/v1/product/{id}")
    @ResponseBody
    public String update(
            @PathVariable Long id,
            @Valid @RequestBody ProductSaveRequestDto requestDto,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "로그인이 필요한 서비스입니다.";
        }
        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

        productService.update(id, requestDto.getTitle(), requestDto.getContent(), requestDto.getPrice(), loginUser.getNickname());
        return "success";
    }

    /**
     * 5. 상품 상태 변경
     */
    @PutMapping("/product/{id}/status")
    public String changeStatusForm(@PathVariable Long id,
                                   @RequestParam String status,
                                   HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }
        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

        String finalStatus = status;
        if ("SALE".equals(status)) {
            finalStatus = ProductStatus.SALE;
        } else if ("RESERVED".equals(status)) {
            finalStatus = ProductStatus.RESERVED;
        } else if ("SOLD".equals(status)) {
            finalStatus = ProductStatus.SOLD;
        }

        productService.changeStatus(id, finalStatus, loginUser.getNickname());
        return "redirect:/product/" + id;
    }

    /**
     * 6. 댓글 등록
     */
    @PostMapping("/product/{productId}/comments")
    public String saveComment(@PathVariable Long productId,
                              @RequestParam String content,
                              HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }
        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
        commentService.save(productId, content, loginUser);
        return "redirect:/product/" + productId;
    }

    /**
     * 7. 댓글 삭제
     */
    @PostMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId,
                                @RequestParam Long productId,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }
        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");

        try {
            commentService.delete(commentId, loginUser);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/product/" + productId;
    }

    // ==========================================
    // 💡 아래부터 이번에 새로 추가된 '결제 관련' 코드입니다!
    // ==========================================

    /**
     * 8. 결제 폼(화면)으로 이동
     */
    @GetMapping("/product/{id}/payment")
    public String paymentForm(@PathVariable Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:/user/login";
        }

        SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
        Product product = productService.findById(id);

        model.addAttribute("product", product);
        model.addAttribute("loginUser", loginUser);

        // 헤더 에러 방지 및 로그인 상태 유지
        model.addAttribute("nickname", loginUser.getNickname());
        model.addAttribute("keyword", "");

        return "product/payment";
    }

    /**
     * 9. 결제 처리 로직 (사용자가 폼에서 '결제하기' 누르면 실행)
     */
    @PostMapping("/product/{id}/payment")
    public String processPayment(@PathVariable Long id) {
        // 원래는 여기에 카드 승인 확인, 상품 상태 '판매완료'로 변경 등의 로직이 들어갑니다.
        return "redirect:/product/" + id + "/payment/success";
    }

    /**
     * 10. 결제 성공(영수증) 화면으로 이동
     */
    @GetMapping("/product/{id}/payment/success")
    public String paymentSuccess(@PathVariable Long id, HttpServletRequest request, Model model) {
        Product product = productService.findById(id);

        // 현재 로그인 유저 정보 확인 (헤더용)
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            SiteUser loginUser = (SiteUser) session.getAttribute("loginUser");
            model.addAttribute("nickname", loginUser.getNickname());
        }

        // 가상의 주문번호 및 현재 결제된 시간 생성
        String orderNumber = "ORD-" + java.time.LocalDate.now().toString().replace("-", "") + "-" + (int)(Math.random() * 1000);
        String paymentTime = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. MM. dd. a hh:mm:ss"));

        model.addAttribute("product", product);
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("keyword", "");

        return "product/payment_success";
    }
}