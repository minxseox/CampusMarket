package com.example.CampusMarket.service;

import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.ProductRepository;
import com.example.CampusMarket.entity.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 1. 상품 등록 로직
     */
    @Transactional
    public Long save(String title, String content, int price) {
        Product product = Product.builder()
                .title(title)
                .content(content)
                .price(price)
                .status(ProductStatus.SALE) // 처음 등록 시 기본값 "판매중"
                .build();

        return productRepository.save(product).getId();
    }

    /**
     * 2. 상품 상세 조회 로직
     */
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + id));
    }

    /**
     * 3. 상품 전체 조회 로직 (메인 화면용)
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * 4. 상품 상태 변경 로직 ([판매중] ➡️ [예약중] ➡️ [판매완료])
     */
    @Transactional
    public void changeStatus(Long id, String newStatus) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + id));

        // 엔티티 내부 메서드를 실행해 상태값을 안전하게 변경 (오타 검증 포함)
        product.updateStatus(newStatus);

        // 🌟 중요: JPA 영속성 컨텍스트 덕분에 따로 repository.save()를 안 쳐도
        // 메서드가 끝날 때 DB의 데이터가 알아서 자동으로 수정됩니다! (Dirty Checking)
    }

    /**
     * 5. 상품 삭제 로직
     */
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + id));

        productRepository.delete(product);
    }
}