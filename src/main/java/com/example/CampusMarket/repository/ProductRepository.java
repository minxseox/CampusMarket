package com.example.CampusMarket.repository;

import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 내가 올린 상품 조회 (마이페이지용)
    List<Product> findByAuthor(SiteUser author);

    // 필터링 및 검색용 메서드들 (이게 없어서 에러가 발생했습니다)
    List<Product> findByCategory(String category);
    List<Product> findByStatus(String status);
    List<Product> findByCategoryAndStatus(String category, String status);
    List<Product> findByTitleContaining(String keyword);
    List<Product> findByCategoryAndTitleContaining(String category, String keyword);
    List<Product> findByStatusAndTitleContaining(String status, String keyword);
    List<Product> findByCategoryAndStatusAndTitleContaining(String category, String status, String keyword);
}