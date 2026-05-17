package com.example.CampusMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 🌟 이거 추가되었는지 확인!
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        // 🌟 'model' 가방에 "userName"이라는 이름으로 "민서" 데이터를 직접 실어서 화면으로 던집니다.
        model.addAttribute("userName", "민서");

        return "index"; // templates/index.mustache로 이동
    }
}