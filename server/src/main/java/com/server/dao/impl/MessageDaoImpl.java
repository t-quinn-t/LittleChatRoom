package com.server.dao.impl;

import com.server.dao.MessageDao;
import com.server.model.Message;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 12:14 p.m.
 */
@Component
public class MessageDaoImpl extends JdbcDaoSupport implements MessageDao {

    public MessageDaoImpl(DataSource dataSource) {this.setDataSource(dataSource);}

    public void save(Message message) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        this.getJdbcTemplate().update(
                "INSERT INTO public.messages (from_user, from_room, message_content) VALUES (?,?,?)",
                message.getSender(),
                message.getRoom(),
                message.getContent()
        );
    }

    public void delete(Message message) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        this.getJdbcTemplate().update(
                "DELETE FROM public.messages WHERE message_id = ?",
                message.getId()
        );
    }

    public List<Message> getMessagesByRoomId(Long roomId) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        return this.getJdbcTemplate().query(
            "SELECT * FROM public.messages WHERE from_room = ?",
                (resultSet, i) -> new Message(
                        resultSet.getString("from_user"),
                        resultSet.getString("from_room"),
                        resultSet.getString("message_content")),
            roomId
        );
    }
}
