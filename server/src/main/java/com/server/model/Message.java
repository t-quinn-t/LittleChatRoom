package com.server.model;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 12:16 p.m.
 */
public class Message {
    private Long id;
    private Long senderId;
    private Long roomId;
    private String content;

    public Message(Long sender, Long room, String content) {
        this.senderId = sender;
        this.roomId = room;
        this.content  = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSender() {
        return senderId;
    }

    public void setSender(Long sender) {
        this.senderId = sender;
    }

    public Long getRoom() {
        return roomId;
    }

    public void setRoom(Long room) {
        this.roomId = room;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
