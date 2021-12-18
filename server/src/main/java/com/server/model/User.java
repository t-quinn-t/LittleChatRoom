package com.server.model;

public class User {
    private Long id;
    private String name;
    private String email;
    private String password;

    public User() {
        this.name = "default";
        this.email = "default@littlechatroom.com";
        this.password = "pwd";
    }

    public User(String uname, String email, String password) {
        this.name = uname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long uid) {
        this.id = uid;
    }
}
