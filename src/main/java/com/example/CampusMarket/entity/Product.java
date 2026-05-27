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

    @Column(nullable = false)
    private String category;

    // 🌟 일반 String 타입으로 변경하여 글자를 그대로 저장합니다!
    @Column(nullable = false)
    private String status;

    // 💡 [추가 완료] 이 상품을 누가 올렸는지 기억하는 작성자 필드! (본인 확인용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private SiteUser author;

    @Builder
    public Product(String title, String content, int price, String status, String category, SiteUser author) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.category = category;
        this.status = (status != null) ? status : ProductStatus.SALE;
        this.author = author;
    }

    /**
     * 💡 [추가 완료] ProductService 107번째 줄의 빨간 줄 에러를 없애줄 바로 그 메서드!
     * 제목, 내용, 가격을 안전하게 변경해 줍니다.
     */
    public void update(String title, String content, int price) {
        this.title = title;
        this.content = content;
        this.price = price;
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

    public boolean isSale() {
        return "판매중".equals(this.status);
    }

    public boolean isReserved() {
        return "예약중".equals(this.status);
    }

    public boolean isSold() {
        return "판매완료".equals(this.status);
    }
}