package com.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/test-connection")
    public String testConnection() {
        return "Successfully connected";
    }

}
