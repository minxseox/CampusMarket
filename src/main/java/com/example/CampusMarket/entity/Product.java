package com.example.CampusMarket.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private int price;

    // 🌟 일반 String 타입으로 변경하여 글자를 그대로 저장합니다!
    @Column(nullable = false)
    private String status;

    @Builder
    public Product(String title, String content, int price, String status) {
        this.title = title;
        this.content = content;
        this.price = price;
        // 🌟 기본값으로 ProductStatus 클래스에 정의해둔 "판매중" 글자를 넣어줍니다.
        this.status = (status != null) ? status : ProductStatus.SALE;
    }

    // [상태 변경 메서드] 수동으로 잘못된 글자가 들어오는 것을 막는 안전장치를 추가했습니다.
    public void updateStatus(String status) {
        if (!status.equals(ProductStatus.SALE) &&
                !status.equals(ProductStatus.RESERVED) &&
                !status.equals(ProductStatus.SOLD)) {
            throw new IllegalArgumentException("올바른 상품 상태(판매중, 예약중, 판매완료)가 아닙니다!");
        }
        this.status = status;
    }
}