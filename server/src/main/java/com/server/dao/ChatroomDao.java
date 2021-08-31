package com.server.dao;

import com.server.model.Chatroom;
import com.server.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomDao {

    Chatroom save(Chatroom chatroom);
    void delete(Long cid);
    void updateChatroom(Chatroom chatroom);
    Chatroom findRoomByIdentifier(Long cid);
    Chatroom findRoomByName(String roomName);
    List<Chatroom> findRoomsByUid(Long uid);
    void registerUserToRoom(User user, Chatroom room);
}
