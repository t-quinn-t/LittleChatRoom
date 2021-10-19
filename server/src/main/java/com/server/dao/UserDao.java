package com.server.dao;

import com.server.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    public void save(User user);
    public void delete(Long uid);
    public void updateUser(User user);

    /**
     * A Generic Search Function
     * @param uid: if -1, then find by using an identifier (name or email, depends on idType)
     *           else, use uid to search for a user
     * @return a single user or null if failed
     */
    public User findByIdentifier(String identifier, String idType, Long uid);
    public void updateUserSettings(User user, String userSettingsJsonStr);
    public String getUserSettings(User user);
}
