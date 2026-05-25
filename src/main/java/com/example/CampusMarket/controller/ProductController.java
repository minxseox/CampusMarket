package com.example.CampusMarket.controller;

import com.example.CampusMarket.dto.ProductSaveRequestDto;
import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.ProductStatus;
import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;

    /**
     * 1. 상품 등록 페이지로 이동
     */
    @GetMapping("/product/save")
    public String productSave() {
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

        // SiteUser 객체를 직접 넘겨 작성자 정보 저장
        productService.save(requestDto.getTitle(), requestDto.getContent(), requestDto.getPrice(), loginUser);

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

        // 작성자 닉네임으로 권한 검증 수행
        productService.update(id, requestDto.getTitle(), requestDto.getContent(), requestDto.getPrice(), loginUser.getNickname());

        return "success";
    }

    /**
     * 5. 상품 상태 변경 (순수 HTML 폼 방식)
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
}