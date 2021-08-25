package com.server.dao.impl;

import com.server.dao.ChatroomDao;
import com.server.model.Chatroom;
import com.server.model.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Component
public class ChatroomDaoImpl extends JdbcDaoSupport implements ChatroomDao {
    public ChatroomDaoImpl(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public Chatroom save(Chatroom chatroom) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        String sql = "INSERT INTO public.chatrooms (room_name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,chatroom.getName());
            return ps;
        }, keyHolder);
        Chatroom storedChatroom = new Chatroom();
        storedChatroom.setName(chatroom.getName());
        storedChatroom.setCid(keyHolder.getKeyAs(Long.class));
        return storedChatroom;
    }

    public void delete(Long cid) {
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

    public Chatroom findRoomByName(String roomName) {
        if (getJdbcTemplate() == null) throw new NullPointerException();
        return DataAccessUtils.singleResult(getJdbcTemplate().query(
                "SELECT * FROM public.chatrooms WHERE room_id = ?",
                (resultSet, i) -> new Chatroom(
                        resultSet.getString("room_name")),
                roomName
        ));
    }


    public List<Chatroom> findRoomsByUid(Long uid) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        String sql = "SELECT room_id, room_name FROM public.chatrooms INNER JOIN public.chatroom_user_mapping ON " +
                "room_id = room_id_fk WHERE user_id_fk = ?";
        return getJdbcTemplate().query(sql, (resultSet, i)-> {
            Chatroom room = new Chatroom();
            room.setCid(resultSet.getLong(1));
            room.setName(resultSet.getString(2));
            return room;
        }, uid);
    }

    public void registerUserToRoom(User user, Chatroom room) {
        if (getJdbcTemplate() == null)
            throw new NullPointerException();
        String sql = "INSERT INTO public.chatroom_user_mapping (user_id_fk, room_id_fk) VALUES(?,?)";
        getJdbcTemplate().update(sql, user.getUid(), room.getCid());
    }
}
