package com.example.CampusMarket.dto;

import com.example.CampusMarket.entity.SiteUser;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class UserForm {
    private String username;
    private String email;
    private String password;
    private String nickname;

    // 엔티티 생성 시 nickname을 함께 넘겨줍니다.
    public SiteUser toEntity() {
        return new SiteUser(null, username, password, email, nickname, null);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
}