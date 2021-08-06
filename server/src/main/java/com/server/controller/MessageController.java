package com.server.controller;

import com.server.dao.MessageDao;
import com.server.dao.UserDao;
import com.server.exception.UserNotFoundException;
import com.server.model.Message;
import com.server.model.User;
import com.server.model_assembler.MessageModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

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
     *
     * The Message that sent to broker would have type <EntityModel<Message>>
     *     the Message itself is of type <Message<EntityModel<Message>>>
     */
    @CrossOrigin(origins = {"http://localhost:3000"})
    @MessageMapping("/send-to/{roomId}/")
    @SendTo("/topic/{roomId}/")
    public EntityModel<Message> sendMessage(@DestinationVariable Long roomId, Message message) {
        Long userId = message.getSenderId();
        User user = userDao.findByIdentifier(null, null, 3L);
        if (user == null)
            throw new UserNotFoundException("unknown");

        // if user found, register this message to repo
        Message registeredMessaage = messageDao.save(message);
        // TODO: replace the roomId with actual roomId
        return messageModelAssembler.toModel(registeredMessaage);
    }
}
