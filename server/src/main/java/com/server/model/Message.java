package com.server.model;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 12:16 p.m.
 */
public class Message {
    private Long id;
    private String sender;
    private Long roomId;
    private String content;

    public Message(String sender, Long roomId, String content) {
        this.sender = sender;
        this.roomId = roomId;
        this.content  = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String uname) {
        this.sender = uname;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long room) {
        this.roomId = room;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


