package com.example.CampusMarket.repository;

import com.example.CampusMarket.entity.Product;
import com.example.CampusMarket.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByAuthor(SiteUser author);
}