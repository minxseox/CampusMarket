package com.example.CampusMarket.service;

import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.dto.UserForm;
import com.example.CampusMarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 중복 검사 로직: 중복이 있으면 에러 메시지(String)를 반환하고, 없으면 null을 반환
    public String checkDuplicate(UserForm form) {
        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            return "이미 사용 중인 아이디입니다.";
        }
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            return "이미 사용 중인 이메일입니다.";
        }
        return null;
    }

    public SiteUser create(UserForm form) {
        SiteUser user = form.toEntity();
        return userRepository.save(user);
    }

    public SiteUser login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    @Transactional
    public SiteUser updateUserInfo(Long id, String email, String nickname) {
        SiteUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.updateProfile(email, nickname);
        return user;
    }
}