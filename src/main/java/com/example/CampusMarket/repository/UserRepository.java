package com.example.CampusMarket.repository;

import com.example.CampusMarket.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);

    // 이메일 중복 검사를 위한 쿼리 메서드
    Optional<SiteUser> findByEmail(String email);
}