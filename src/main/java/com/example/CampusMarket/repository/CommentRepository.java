package com.example.CampusMarket.repository;

import com.example.CampusMarket.entity.Comment;
import com.example.CampusMarket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProductOrderByIdDesc(Product product);
}