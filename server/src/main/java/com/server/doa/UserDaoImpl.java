package com.server.doa;

import com.server.model.User;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    public UserDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void save(User user) {
        try {
            getJdbcTemplate().update(
                    "INSERT INTO public.user (uname, pwd, email) VALUES (?, ?, ?)",
                    user.getName(),
                    user.getPassword(),
                    user.getEmail()
            );
        } catch (NullPointerException e) {
            return;
        }
    }

    public void delete(String uid) {
        return;
    }

    public User updatePassword(String uid, String oldPassword, String newPassword) {
        return null;
    }

    public User updateProfile(String uid, String uname, String email) {
        return null;
    }

    public User findByName(String uname) {
        return null;
    }

    public User findByEmail(String email) {
        return null;
    }
}
