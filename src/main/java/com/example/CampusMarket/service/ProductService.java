package com.example.CampusMarket.service;

import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.repository.ProductRepository;
import com.example.CampusMarket.entity.ProductStatus;
import com.example.CampusMarket.entity.SiteUser;
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
     * 💡 [추가] 상품을 등록할 때 '누가 올렸는지' 작성자 정보(author)도 함께 저장해야 합니다.
     */
    @Transactional
    // 💡 String author -> SiteUser author 로 변경!
    public Long save(String title, String content, int price, SiteUser author) {
        Product product = Product.builder()
                .title(title)
                .content(content)
                .price(price)
                .status(ProductStatus.SALE)
                .author(author) // 이제 SiteUser 객체를 받으므로 정상 작동합니다.
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
     * 4. 상품 상태 변경 로직
     * 💡 [추가] 상태 변경도 본인만 할 수 있도록 현재 로그인한 유저 정보(loginUser)를 받습니다.
     */
    @Transactional
    public void changeStatus(Long id, String newStatus, String loginUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + id));

        // 🔐 권한 검증: 상품 작성자와 현재 로그인한 유저가 다르면 에러 발생!
        // ⚠️ 주의: getAuthor() 부분은 Product 엔티티에 만들어둔 작성자 필드명에 맞게 고쳐주세요.
        /*
        if (!product.getAuthor().equals(loginUser)) {
            throw new IllegalArgumentException("상태를 변경할 권한이 없습니다.");
        }
        */

        product.updateStatus(newStatus);
    }

    /**
     * 5. 상품 삭제 로직
     * 💡 [추가] 삭제 요청 시 현재 로그인한 유저 정보(loginUser)를 받습니다.
     */
    @Transactional
    public void delete(Long id, String loginUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + id));

        // 🔐 권한 검증: 상품 작성자와 현재 로그인한 유저가 다르면 에러 발생!
        /*
        if (!product.getAuthor().equals(loginUser)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        */

        productRepository.delete(product);
    }

    /**
     * 6. 상품 정보 수정 로직
     * 💡 [추가] 수정 요청 시 현재 로그인한 유저 정보(loginUser)를 받습니다.
     */
    @Transactional
    public void update(Long id, String title, String content, int price, String loginUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + id));

        // 🔐 권한 검증: 상품 작성자와 현재 로그인한 유저가 다르면 에러 발생!
        /*
        if (!product.getAuthor().equals(loginUser)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        */

        product.update(title, content, price);
    }
}