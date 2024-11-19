package com.project.oop.PMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/auth")
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Tên file HTML trong thư mục templates
    }
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Tên file HTML trong thư mục templates
    }
}
