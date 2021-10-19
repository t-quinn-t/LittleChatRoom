package com.server.dao.impl;

import com.server.dao.UserDao;
import com.server.model.User;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

    public void delete(Long uid) throws NullPointerException {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "DELETE FROM public.users WHERE uid = ?",
                uid
        );
    }

    public void updateUser(User user) throws NullPointerException {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "UPDATE public.users SET " +
                        "uname=?, " +
                        "email=?, " +
                        "pwd=? " +
                        "WHERE uid=?",
                user.getName(), user.getEmail(), user.getPassword(), user.getUid()
        );
    }

    public User findByIdentifier(String identifier, String idType, Long uid) {

        User user;
        try {
            if (getJdbcTemplate() == null) throw new NullPointerException();
            if (uid == -1 || idType != null) {
                String ps = "SELECT * FROM public.users WHERE " + idType + " = ?";
                user = DataAccessUtils.singleResult( getJdbcTemplate().query(ps,
                        (resultSet, i) -> {
                            User user1 = new User();
                            user1.setName(resultSet.getString("uname"));
                            user1.setEmail(resultSet.getString("email"));
                            user1.setPassword(resultSet.getString("pwd"));
                            user1.setUid(resultSet.getLong("uid"));
                            return user1;
                        },
                        identifier));
            }
            else {
                String ps = "SELECT * FROM public.users WHERE uid = ?";
                user = DataAccessUtils.singleResult( getJdbcTemplate().query(ps,
                        (resultSet, i) -> {
                            User user1 = new User();
                            user1.setName(resultSet.getString("uname"));
                            user1.setEmail(resultSet.getString("email"));
                            user1.setPassword(resultSet.getString("pwd"));
                            user1.setUid(resultSet.getLong("uid"));
                            return user1;
                        },
                        uid));
            }

        } catch (NullPointerException e) {
            user = null;
        }
        return user;
    }

    @Override
    public void updateUserSettings(User user, String userSettingsJsonStr) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        String ps = "INSERT INTO public.user_settings(user_id_fk, settings_data)" +
                "VALUES(?, ?::JSONB)" +
                "ON CONFLICT (user_id_fk)" +
                "DO UPDATE SET settings_data = ?::JSONB";
        getJdbcTemplate().update(ps, user.getUid(), userSettingsJsonStr, userSettingsJsonStr);

    }

    @Override
    public String getUserSettings(User user) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        String ps = "SELECT settings_data FROM public.user_settings WHERE user_id_fk = ?";
        return DataAccessUtils.singleResult(getJdbcTemplate().query(ps,
                (resultSet, i) -> resultSet.getString(1),
                user.getUid()));
    }
}
