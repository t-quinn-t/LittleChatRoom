package com.server.dao;

public interface UserDao {
    UserDao get(String username);
    void save(String username, String pwd, String email);
    void update(String username);
    void delete(String username);
}
