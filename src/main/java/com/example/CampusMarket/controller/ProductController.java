package com.example.CampusMarket.controller;

import com.example.CampusMarket.dto.ProductSaveRequestDto;
import com.example.CampusMarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
        return "product_save";
    }

    /**
     * 2. 상품 등록 실행 (API)
     */
    @PostMapping("/api/v1/product")
    @ResponseBody
    public String save(@Valid @RequestBody ProductSaveRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        productService.save(requestDto.getTitle(), requestDto.getContent(), requestDto.getPrice());
        return "success";
    }

    // 💡 스프링 부트 중복 주소 충돌 방지를 위해 3번 productDetail 메서드는
    // 예진 언니가 합쳐놓은 IndexController에 양보하고 여기서는 삭제 완료했습니다!
}