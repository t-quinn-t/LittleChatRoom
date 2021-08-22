package com.server.dao;

import com.server.model.Chatroom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomDao {

    void save(Chatroom chatroom);
    void delete(String cid);
    void updateChatroom(Chatroom chatroom);
    Chatroom findRoomByIdentifier(Long cid);
    List<Chatroom> findRoomsByUid(Long uid);
}
