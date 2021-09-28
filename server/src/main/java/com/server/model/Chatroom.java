package com.server.model;

public class Chatroom {
    private long cid;
    private String cname;

    public Chatroom() {
        this.cname = "default chatroom";
    }

    public Chatroom(String name) {
        this.cname = name;
    }

    public Chatroom(long cid, String name) {this.cid = cid; this.cname = name;}

    public String getName() {
        return cname;
    }

    public void setName(String name) {
        this.cname = name;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long id) {
        this.cid = id;
    }
}
