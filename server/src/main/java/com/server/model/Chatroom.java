package com.server.model;

public class Chatroom {
    private long roomId;
    private String roomName;

    public Chatroom() {
        this.roomName = "default chatroom";
    }

    public Chatroom(String name) {
        this.roomName = name;
    }

    public Chatroom(long roomId, String roomName) {this.roomId = roomId; this.roomName = roomName;}

    public String getName() {
        return roomName;
    }

    public void setName(String name) {
        this.roomName = name;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
