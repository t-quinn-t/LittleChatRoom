package com.server.model;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 12:16 p.m.
 */
public class Message {
    private Long id;
    private String sender;
    private String room;
    private String content;

    public Message(String sender, String room, String content) {
        this.sender = sender;
        this.room = room;
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

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
