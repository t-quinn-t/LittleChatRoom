package com.server.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.server.dao.MessageDao;
import com.server.dao.UserDao;
import com.server.exception.UserNotFoundException;
import com.server.model.Message;
import com.server.model.User;
import com.server.model_assembler.MessageModelAssembler;
import com.server.service.JWTAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 11:57 a.m.
 */
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
public class MessageController {

    private final UserDao userDao;
    private final JWTAuthService jwtAuthService;
    private final MessageDao messageDao;
    private final MessageModelAssembler messageModelAssembler;
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    public MessageController(UserDao userDao, MessageDao messageDao, MessageModelAssembler messageModelAssembler,
                             @Qualifier("jwtservice") JWTAuthService jwtAuthService) {
        this.userDao = userDao;
        this.messageDao = messageDao;
        this.messageModelAssembler = messageModelAssembler;
        this.jwtAuthService = jwtAuthService;
    }

    /**
     * Receiving messages at endpoint /send-message, and
     * send it to destination at /topic/{proper chatroom} so that every subscriber to this chatroom will get
     * notification
     *
     * The Message that sent to broker would have type <EntityModel<Message>>
     *     the Message itself is of type <Message<EntityModel<Message>>>
     */
    @CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = "token")
    @MessageMapping("/send-to/{roomId}/")
    @SendTo("/topic/{roomId}/")
    public EntityModel<Message> sendMessage(@DestinationVariable Long roomId, Message message, @Header String token,
                                            @Header byte[] publicKey) {
        logger.info("Message Received from chatroom:" + roomId + "/n" + Arrays.toString(publicKey));
        Long userId = message.getSenderId();
        User user = userDao.findByIdentifier(null, null, userId);
        if (user == null)
            throw new UserNotFoundException("unknown");
        logger.info("Verifying Token");
        if (jwtAuthService.verifyToken(token, publicKey, user)) {
            Message registeredMessage = messageDao.save(message);
            logger.info("Message sending to broker under /topic/"+roomId);
            return messageModelAssembler.toModel(registeredMessage);
        }
        else {
            throw new TokenExpiredException("");
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/get-messages/{roomId}")
    public CollectionModel<EntityModel<Message>> getMessagesFromRoom(@PathVariable Long roomId) {
        logger.info("Get all messages from chatroom:" + roomId);
        List<Message> messagesFromRoom = messageDao.getMessagesByRoomId(roomId);
        System.out.println("sending message lists");
        logger.info("Responding with list of messages from chatroom:" + roomId);
        return messageModelAssembler.toCollectionModel(messagesFromRoom);
    }

}
