package com.server.doa;

import com.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    public UserDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public boolean save(User user) {
        getJdbcTemplate().update(
                "INSERT INTO public.user (uname, pwd, email) VALUES (?, ?, ?)",
                user.getName(),
                user.getPwd(),
                user.getEmail()
                );
        return false;
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
