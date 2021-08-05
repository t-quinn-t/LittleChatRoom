package com.server.dao;

import com.server.model.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 12:13 p.m.
 */
@Repository
public interface MessageDao {
    public Message save(Message message);
    public void delete(Message message);
    public Message getMessageByMessageId(Long messageId);
    public List<Message> getMessagesByRoomId(Long roomId);
}
