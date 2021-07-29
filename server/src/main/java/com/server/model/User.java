package com.server.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class User {

    private Long uid;
    private String uname;
    private String email;
    private String password;

    public User() {
        this.uname = "default";
        this.email = "default@littlechatroom.com";
        this.password = "pwd";
    }

    public User(String uname, String email, String password) {
        this.uname = uname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return uname;
    }

    public void setName(String name) {
        this.uname = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
