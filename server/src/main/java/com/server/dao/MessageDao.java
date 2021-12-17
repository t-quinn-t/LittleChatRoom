package com.server.dao;
/**
 * @author Quinn Tao
 */

import com.server.model.Message;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageDao {
    public Message          save(Message message);
    public void             delete(Message message);
    public Message          getMessageByMessageId(Long messageId);
    public List<Message>    getMessagesByRoomId(Long roomId);
}
