package com.server.controller;

import com.server.dao.UserDao;
import com.server.exception.CredentialFailureException;
import com.server.exception.UserNotFoundException;
import com.server.model.User;
import com.server.model_assembler.UserModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserModelAssembler assembler;

    @Autowired
    public UserController(UserDao userDao, BCryptPasswordEncoder passwordEncoder, UserModelAssembler assembler) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.assembler = assembler;
    }

    @GetMapping("/test-connection")
    public String testConnection() {
        String saltedPassword = passwordEncoder.encode("888");
        User testUser = new User();
        testUser.setPassword(saltedPassword);
        userDao.save(testUser);
        return "Successfully connected";
    }

    @PostMapping("/register")
    public String registerNewUser(@RequestParam String uname, @RequestParam String email,
                                  @RequestParam CharSequence password) {
        String saltedPassword = passwordEncoder.encode(password);
        User newUser = new User(uname, email, saltedPassword);
        userDao.save(newUser);
        return "New User Registered";
    }

    @GetMapping("/login")
    public EntityModel<User> login(@RequestParam String identifier, @RequestParam CharSequence password) {

        // find user
        User locatedUser = userDao.findByIdentifier(identifier, "uname");
        if (locatedUser == null)
            locatedUser = userDao.findByIdentifier(identifier, "email");
        if (locatedUser == null)
            throw new UserNotFoundException(identifier);

        // user password match-up
        if (!passwordEncoder.matches(password, locatedUser.getPassword()))
            throw new CredentialFailureException();

        // login succeed
        return assembler.toModel(locatedUser);
    }


}
