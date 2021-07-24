package com.server.controller;

import com.server.doa.UserDao;
import com.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserDao userDao;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/test-connection")
    public String testConnection() {
        User testUser = new User("Awesome Quinn","333","qtao@littlechatroom.com");
        userDao.save(testUser);
        return "Successfully connected";
    }

    @PostMapping("/register")
    public String registerNewUser(@RequestParam String uname, @RequestParam String email,
                                  @RequestParam CharSequence password) {
        String saltedPassword = passwordEncoder.encode(password);
        User newUser = new User(uname, email, saltedPassword);
        userDao.save(newUser);
        return "stub";
    }
}
