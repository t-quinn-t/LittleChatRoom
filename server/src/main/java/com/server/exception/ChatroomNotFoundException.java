package com.server.exception;

public class ChatroomNotFoundException extends RuntimeException{
    public ChatroomNotFoundException(String problem) {
        super(problem.equals("unknown") ?
                "Oops, seems like you are disconnected!" :
                "Chatroom with given information cannot be found:" + problem);
    }
}
