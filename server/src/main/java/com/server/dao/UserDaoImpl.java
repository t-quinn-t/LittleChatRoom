package com.server.dao;

import com.server.model.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    public UserDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void save(User user) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "INSERT INTO public.users (uname, pwd, email) VALUES (?, ?, ?)",
                user.getName(),
                user.getPassword(),
                user.getEmail()
        );
    }

    public void delete(String uname) throws NullPointerException {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "DELETE FROM public.users WHERE uname = ?",
                uname
        );
    }

    public void updateUser(User user) throws NullPointerException {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "UPDATE public.user " +
                        "SET uname = ?" +
                        "SET email = ?" +
                        "SET password = ?",
                user.getName(), user.getEmail(), user.getPassword()
        );
    }

    public User findByIdentifier(String identifier, String idType) {
        User user;
        try {
            String ps = "SELECT * FROM public.users WHERE " + idType + " = ?";
            if (getJdbcTemplate() == null) throw new NullPointerException();
            user = DataAccessUtils.singleResult( getJdbcTemplate().query(ps,
                    (resultSet, i) -> {
                        User user1 = new User();
                        user1.setName(resultSet.getString("uname"));
                        user1.setEmail(resultSet.getString("email"));
                        user1.setPassword(resultSet.getString("pwd"));
                        return user1;
                    },
                    identifier));
        } catch (NullPointerException e) {
            user = null;
        }
        return user;
    }

}
