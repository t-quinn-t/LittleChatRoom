package com.server.controller;

import com.server.dao.MessageDao;
import com.server.dao.UserDao;
import com.server.model.Message;
import com.server.model.User;
import com.server.model_assembler.MessageModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 11:57 a.m.
 */
@Controller
public class MessageController {

    private final UserDao userDao;
    private final MessageDao messageDao;
    private final MessageModelAssembler messageModelAssembler;

    @Autowired
    public MessageController(UserDao userDao, MessageDao messageDao, MessageModelAssembler messageModelAssembler) {
        this.userDao = userDao;
        this.messageDao = messageDao;
        this.messageModelAssembler = messageModelAssembler;
    }

    /**
     * Receiving messages at endpoint /send-message, and
     * send it to destination at /topic/{proper chatroom} so that every subscriber to this chatroom will get
     * notification
     */
    @CrossOrigin(origins = {"http://localhost:3000"})
    @MessageMapping("/send-message")
    @SendTo("/topic/{roomId}")
    public EntityModel<Message> sendMessage(@RequestParam String message_content, @RequestParam Long userId,
                                            @RequestParam Long roomId) {
        System.out.println("message received");
        User user = userDao.findByIdentifier(null, null, userId);
        // TODO: replace the roomId with actual roomId
        Message message = new Message(user.getUid(), 1L, message_content);
        return messageModelAssembler.toModel(message);
    }
}
