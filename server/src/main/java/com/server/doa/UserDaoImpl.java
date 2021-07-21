package com.server.doa;

import com.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    @Autowired
    JdbcTemplate template;

    public User save(User user) {

        return user;
    }

    public boolean delete(String uid) {
        return false;
    }

    public User updatePassword(String uid, String oldPassword, String newPassword) {
        return null;
    }

    public User updateProfile(String uid, String uname, String email) {
        return null;
    }

    public User findById(String uid) {
        return null;
    }
}
