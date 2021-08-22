package com.server.dao.impl;

import com.server.dao.ChatroomDao;
import com.server.model.Chatroom;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

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

    public List<Chatroom> findRoomsByUid(Long uid) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        String sql = "SELECT room_id, room_name FROM public.chatrooms INNER JOIN public.chatroom_user_mapping ON " +
                "chatroom_id = room_id_fk WHERE user_id_fk = ?";
        return getJdbcTemplate().query(sql, (resultSet, i)-> {
            Chatroom room = new Chatroom();
            room.setCid(resultSet.getLong(1));
            room.setName(resultSet.getString(2));
            return room;
        }, uid);
    }
}
