package com.server.dao;

import com.server.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    public void save(User user);
    public void delete(String uname);
    public void updateUser(User user);
    public User findByIdentifier(String identifier, String idType);

}
