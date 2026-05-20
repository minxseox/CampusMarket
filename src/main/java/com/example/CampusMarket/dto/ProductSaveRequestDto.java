package com.example.CampusMarket.dto;

import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductSaveRequestDto {

    // 🌟 @NotBlank: null, 빈 문자열(""), 공백(" ") 모두 허용하지 않습니다.
    @NotBlank(message = "상품 제목은 필수 입력 항목입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해 주세요.")
    private String title;

    @NotBlank(message = "상품 설명은 필수 입력 항목입니다.")
    private String content;

    // 🌟 @PositiveOrZero: 0원 또는 양수만 허용합니다. (마이너스 금액 차단!)
    @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    private int price;

    @Builder
    public ProductSaveRequestDto(String title, String content, int price) {
        this.title = title;
        this.content = content;
        this.price = price;
    }

    /**
     * 가방에 담긴 안전한 데이터를 진짜 DB 저장용 'Product 엔티티' 변신시키는 메서드
     */
    public Product toEntity() {
        return Product.builder()
                .title(title)
                .content(content)
                .price(price)
                .status(ProductStatus.SALE) // 첫 등록은 언제나 "판매중" 클래스 상수 사용
                .build();
    }
}