package com.server.dao.impl;

import com.server.dao.MessageDao;
import com.server.model.Message;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 12:14 p.m.
 */
@Component
public class MessageDaoImpl extends JdbcDaoSupport implements MessageDao {

    public MessageDaoImpl(DataSource dataSource) {this.setDataSource(dataSource);}

    public Message save(Message message) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        String sqlStr = "INSERT INTO public.messages (from_user, from_room, message_content) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlStr);
            ps.setLong(1, message.getSenderId());
            ps.setLong(2, message.getRoomId());
            ps.setString(3, message.getContent());
            return ps;
        }, keyHolder);
        message.setId(keyHolder.getKeyAs(Long.class));
        return message;
    }

    public void delete(Message message) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        this.getJdbcTemplate().update(
                "DELETE FROM public.messages WHERE message_id = ?",
                message.getId()
        );
    }

    public Message getMessageByMessageId(Long messageId) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        return DataAccessUtils.singleResult(getJdbcTemplate().query(
                "SELECT * FROM public.messages WHERE from_room = ?",
                (resultSet, i) -> new Message(
                        resultSet.getLong("from_user"),
                        resultSet.getLong("from_room"),
                        resultSet.getString("message_content")),
                messageId
        ));
    }

    public List<Message> getMessagesByRoomId(Long roomId) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        return this.getJdbcTemplate().query(
            "SELECT * FROM public.messages WHERE from_room = ?",
                (resultSet, i) -> new Message(
                        resultSet.getLong("from_user"),
                        resultSet.getLong("from_room"),
                        resultSet.getString("message_content")),
            roomId
        );
    }
}
