package com.server.doa;

import com.server.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    public void save(User user);
    public void delete(String uid);
    public User updatePassword(String uid, String oldPassword, String newPassword);
    public User updateProfile(String uid, String uname, String email);
    public User findByName(String uname);
    public User findByEmail(String email);

}
