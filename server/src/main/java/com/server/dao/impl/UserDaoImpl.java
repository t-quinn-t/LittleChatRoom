package com.server.dao.impl;

import com.server.dao.UserDao;
import com.server.model.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

import javax.sql.DataSource;

@Component
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    public UserDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void save(User user) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        getJdbcTemplate().update(
                "INSERT INTO public.users (user_name, password, email) VALUES (?, ?, ?)",
                user.getName(), user.getPassword(), user.getEmail());
    }

    public void delete(User user) throws NullPointerException {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        getJdbcTemplate().update("DELETE FROM public.users WHERE user_id = ?", user.getId());
    }

    public void updateUser(User user) throws NullPointerException {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        getJdbcTemplate().update(
                "UPDATE public.users SET " + "user_name=?, " + "email=?, " + "password=? "
                        + "WHERE user_id=?",
                user.getName(), user.getEmail(), user.getPassword(), user.getId());
    }

    public User getUserByUserName(String userName) {
        return getUserBySqlStr(buildGetUserSql(), "user_name", userName);
    }

    public User getUserByUserId(Long userId) {
        return getUserBySqlStr(buildGetUserSql(), "user_id", userId);
    }

    public User getUserByEmail(String email) {
        return getUserBySqlStr(buildGetUserSql(), "email", email);
    }

    @Override
    public void updateUserSettings(User user, String userSettingsJsonStr) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        String ps = "INSERT INTO public.user_settings(user_id_fk, settings_data)"
                + "VALUES(?, ?::JSONB)" + "ON CONFLICT (user_id_fk)"
                + "DO UPDATE SET settings_data = ?::JSONB";
        getJdbcTemplate().update(ps, user.getId(), userSettingsJsonStr, userSettingsJsonStr);

    }

    @Override
    public String getUserSettings(User user) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        String ps = "SELECT settings_data FROM public.user_settings WHERE user_id_fk = ?";
        return DataAccessUtils.singleResult(getJdbcTemplate().query(ps,
                (resultSet, i) -> resultSet.getString(1), user.getId()));
    }

    /* ===== ===== ===== Private Helper Functions ===== ===== ===== */
    private String buildGetUserSql() {
        return "SELECT * FROM public.users WHERE ? = ?";
    }

    private User getUserBySqlStr(String sqlStr, String selectColName, Object filterVal) {
        return DataAccessUtils.singleResult(getJdbcTemplate().query(sqlStr, (resultSet, i) -> {
            User user1 = new User();
            user1.setName(resultSet.getString("user_name"));
            user1.setEmail(resultSet.getString("email"));
            user1.setPassword(resultSet.getString("password"));
            user1.setId(resultSet.getLong("user_id"));
            return user1;
        }, selectColName, filterVal));
    }

    /* ===== ===== ===== ECDSA Key Pair related ===== ===== ===== */ 
    public void updateUserPrivateKey(User user, byte[] privateKeyByteVal) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        String sqlstr = "UPDATE public.users SET private_key = ? WHERE users.user_id = ?";
        getJdbcTemplate().update(sqlstr, privateKeyByteVal, user.getId());
    }

    public byte[] getUserPrivateKey(User user) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        String sqlstr = "SELECT * FROM public.users WHERE user_id = ?";
        return DataAccessUtils.singleResult(getJdbcTemplate().query(sqlstr, (resultSet, i) -> {
            byte[] r = resultSet.getBytes("private_key");
                return r;
        }));
    }
}
