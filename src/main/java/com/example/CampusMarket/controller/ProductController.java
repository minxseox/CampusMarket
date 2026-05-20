package com.example.CampusMarket.controller;

import com.example.CampusMarket.dto.ProductSaveRequestDto;
import com.example.CampusMarket.service.ProductService;
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
     * URL: GET http://localhost:8080/product/save
     */
    @GetMapping("/product/save")
    public String productSave() {
        // templates/product/product-save.mustache 파일을 찾아 화면을 띄웁니다.
        return "product/product-save";
    }

    /**
     * 2. 상품 등록 실행 (API)
     * URL: POST http://localhost:8080/api/v1/product
     */
    @PostMapping("/api/v1/product")
    @ResponseBody // 화면 이동이 아니라 데이터(JSON/Text)만 리턴하기 위해 사용합니다.
    public String save(@Valid @RequestBody ProductSaveRequestDto requestDto, BindingResult bindingResult) {

        // 🌟 [@Valid 유효성 검증 체킹] 제목이 비었거나 가격이 마이너스면 여기 걸립니다!
        if (bindingResult.hasErrors()) {
            // DTO에 적어둔 첫 번째 에러 메시지("상품 제목은 필수 항목입니다" 등)를 꺼내서 화면으로 보냅니다.
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }

        // 검증 통과 시 서비스 로직 호출해서 DB에 저장!
        productService.save(requestDto.getTitle(), requestDto.getContent(), requestDto.getPrice());

        return "success"; // 성공하면 화면단(JavaScript)에 success라는 글자를 던져줍니다.
    }
}