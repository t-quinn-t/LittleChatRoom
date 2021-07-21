package com.server.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.annotation.Documented;

@Entity
public class User {

    @Id
    private Long uid;

    private String uname;
    private String email;
    private String pwd;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
