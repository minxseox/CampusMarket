package com.example.CampusMarket.service;

import com.example.CampusMarket.entity.SiteUser;
import com.example.CampusMarket.dto.UserForm;
import com.example.CampusMarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SiteUser create(UserForm form) {
        SiteUser user = form.toEntity();
        return userRepository.save(user);
    }

    public SiteUser login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
}