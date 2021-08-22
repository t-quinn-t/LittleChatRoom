package com.server.exception;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-21 9:46 p.m.
 */
public class ChatRoomAlreadyExistsException extends RuntimeException {
    public ChatRoomAlreadyExistsException(String roomName) {
        super(roomName + " already exists in the system");
    }
}
