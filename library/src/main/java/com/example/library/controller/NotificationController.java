package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {

    @GetMapping("/notifications")
    public String notifications() {
        return "notifications";
    }
}
