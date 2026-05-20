package com.example.CampusMarket.entity;

import org.springframework.data.jpa.repository.JpaRepository;

// 🌟 JpaRepository<엔티티클래스명, ID타입>을 상속받으면 모든 DB 기본 SQL문이 공짜로 생깁니다!
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 이 비어있는 공간에 스프링이 save(), findById(), delete() 같은 핵심 메서드들을 자동으로 구현해 줍니다.
}