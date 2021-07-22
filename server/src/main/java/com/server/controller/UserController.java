package com.server.controller;

import com.server.doa.UserDao;
import com.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;

    @GetMapping("/test-connection")
    public String testConnection() {
        User testUser = new User();
        testUser.setName("Awesome Quinn");
        testUser.setPwd("333");
        testUser.setEmail("qtao@littlechatroom.com");
        userDao.save(testUser);
        return "Successfully connected";
    }

}
