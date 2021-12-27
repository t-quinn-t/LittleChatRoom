package com.server.dao;

import com.server.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    public void     save(User user);
    public void     delete(User user);
    public void     updateUser(User user);
    public User     getUserByUserName(String userName);
    public User     getUserByUserId(Long userId);
    public User     getUserByEmail(String email);
    public void     updateUserSettings(User user, String userSettingsJsonStr);
    public String   getUserSettings(User user);

    // ECDSA Private Key related dao apis
    public void     updateUserPrivateKey(User user, byte[] privateKeyByteVal);
    public byte[]   getUserPrivateKey(User user);
    public void     removeUserPrivateKey(User user);
}


