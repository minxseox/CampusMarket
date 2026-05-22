package com.example.CampusMarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addCommonAttributes(Model model, Principal principal, HttpServletRequest request) {
        // 모든 Mustache 템플릿에서 로그인한 사용자 이름({{username}})을 사용할 수 있도록 설정
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        // 모든 Mustache 폼에서 CSRF 토큰({{_csrf.token}})을 사용할 수 있도록 설정
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
    }
}