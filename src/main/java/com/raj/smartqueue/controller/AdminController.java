package com.raj.smartqueue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "admin-login";
    }
}