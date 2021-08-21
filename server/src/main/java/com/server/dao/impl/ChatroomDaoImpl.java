package com.server.dao.impl;

import com.server.dao.ChatroomDao;
import com.server.model.Chatroom;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ChatroomDaoImpl extends JdbcDaoSupport implements ChatroomDao {
    public ChatroomDaoImpl(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void save(Chatroom chatroom) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "INSERT INTO public.chatrooms (room_id, room_name) VALUES (?, ?)",
                chatroom.getCid(),
                chatroom.getName()
        );

    }
    public void delete(String cid) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "DELETE FROM public.chatrooms WHERE cid = ?",
                cid
        );
    }
    public void updateChatroom(Chatroom chatroom) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        getJdbcTemplate().update(
                "UPDATE public.chatrooms " +
                        "SET room_name=?" +
                        "WHERE room_id=?",
                chatroom.getName(),
                chatroom.getCid()
        );
    }
    public Chatroom findRoomByIdentifier(Long cid) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        return DataAccessUtils.singleResult(getJdbcTemplate().query(
                "SELECT * FROM public.chatrooms WHERE room_id = ?",
                (resultSet, i) -> new Chatroom(
                        resultSet.getString("room_name")),
                cid
        ));
    }
}
