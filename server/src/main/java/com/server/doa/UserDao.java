package com.server.doa;

import com.server.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    public User save(User user);
    public boolean delete(String uid);
    public User updatePassword(String uid, String oldPassword, String newPassword);
    public User updateProfile(String uid, String uname, String email);
    public User findById(String uid);

}
