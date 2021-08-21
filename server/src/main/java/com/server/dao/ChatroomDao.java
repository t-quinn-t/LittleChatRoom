package com.server.dao;

import com.server.model.Chatroom;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomDao {

    void save(Chatroom chatroom);
    void delete(String cid);
    void updateChatroom(Chatroom chatroom);
    Chatroom findRoomByIdentifier(Long cid);
}
