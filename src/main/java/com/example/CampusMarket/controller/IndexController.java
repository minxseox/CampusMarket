package com.example.CampusMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    /* @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("userName", "민서");

        return "index"; // templates/index.mustache로 이동
    } */

    @GetMapping("/")
    public String index(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String status,
        Model model
        ) {
            /* List<Product> products = productService.findByFilter(category, status);

            model.addAttribute("products", products); */

            model.addAttribute("userName", "강냉이");
            model.addAttribute("category", category);
            model.addAttribute("status", status);

        return "index";
        }
    
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        
        return "product/detail";
        }
}