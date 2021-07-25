package com.server.dao;

import com.server.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    public UserDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void save(User user) throws NullPointerException {

        getJdbcTemplate().update(
                "INSERT INTO public.user (uname, pwd, email) VALUES (@_uname, ?, ?) WHERE NOT EXISTS " +
                            "(SELECT * FROM public.user WHERE uname = @_uname)",
                user.getName(),
                user.getPassword(),
                user.getEmail()
        );

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

    public User findByIdentifier(String identifier, String idType) {
        User ret = new User();

        getJdbcTemplate().query("SELECT * FROM public.user WHERE " + idType + " ?",
                new Object[]{identifier}, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet resultSet) throws SQLException {
                        ret.setName(resultSet.getString("uname"));
                        ret.setEmail(resultSet.getString("email"));
                        ret.setPassword(resultSet.getString("pwd"));
                    }
                });
        return ret;
    }

}
