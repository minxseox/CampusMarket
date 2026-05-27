package com.example.CampusMarket.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByStatus(String status);

    List<Product> findByCategoryAndStatus(String category, String status);

    List<Product> findByTitleContaining(String keyword);

    List<Product> findByCategoryAndTitleContaining(String category, String keyword);

    List<Product> findByStatusAndTitleContaining(String status, String keyword);

    List<Product> findByCategoryAndStatusAndTitleContaining(String category, String status, String keyword);
}