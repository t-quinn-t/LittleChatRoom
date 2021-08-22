package com.server.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.server.dao.ChatroomDao;
import com.server.dao.UserDao;
import com.server.exception.ChatRoomAlreadyExistsException;
import com.server.exception.ChatroomNotFoundException;
import com.server.exception.UserNotFoundException;
import com.server.model.Chatroom;
import com.server.model.User;
import com.server.model_assembler.ChatroomModelAssembler;
import com.server.service.JWTAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:3000"})
@RestController
@RequestMapping("/chatroom")
public class ChatroomController {

    private final ChatroomDao chatroomDao;
    private final ChatroomModelAssembler chatroomModelAssembler;
    private final UserDao userDao;
    private final JWTAuthService jwtAuthService;
    private final Logger logger = LoggerFactory.getLogger(ChatroomController.class);

    @Autowired
    public ChatroomController(ChatroomDao chatroomDao, ChatroomModelAssembler chatroomModelAssembler,
                               @Qualifier("jwtservice") JWTAuthService jwtAuthService, UserDao userDao) {
        this.chatroomDao = chatroomDao;
        this.chatroomModelAssembler = chatroomModelAssembler;
        this.jwtAuthService = jwtAuthService;
        this.userDao = userDao;
    }

    @PostMapping("/update")
    public EntityModel<Chatroom> updateName(@RequestParam Long cid, @RequestParam(required = false) String name) {
        Chatroom currChatroom = chatroomDao.findRoomByIdentifier(cid);
        if (currChatroom == null)
            throw new ChatroomNotFoundException("unknown");
        if (name != null)
            currChatroom.setName(name);
        chatroomDao.updateChatroom(currChatroom);
        return chatroomModelAssembler.toModel(currChatroom);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @GetMapping("/get-user-rooms")
    public CollectionModel<EntityModel<Chatroom>> getUserRegisteredRooms(@RequestParam Long uid,
                                                                         @RequestHeader String token,
                                                                         @RequestHeader byte[] publicKey) {
        logger.info("User " + uid + " requests list of registered rooms");
        /* ===== ===== ===== Check user sanity ===== ===== ===== */
        logger.debug("Locating user entity");
        User locatedUser = userDao.findByIdentifier(null, null, uid);
        if (locatedUser == null)
            throw new UserNotFoundException("unknown");

        /* ===== ===== ===== Verify JWT ===== ===== ===== */
        logger.debug("Verifying token");
        if (!jwtAuthService.verifyToken(token, publicKey, locatedUser))
            throw new TokenExpiredException("unknown");

        /* ===== ===== ===== Get room list ===== ===== ===== */
        logger.debug("Collecting user registered rooms");
        List<Chatroom> userRegisteredRoom = chatroomDao.findRoomsByUid(uid);
        return chatroomModelAssembler.toCollectionModel(userRegisteredRoom);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @GetMapping("/add-user-to-room")
    public String addUserToRoom(@RequestParam String roomName, @RequestParam Long uid, @RequestHeader String token,
                                @RequestHeader byte[] publicKey) {
        logger.info("User " + uid + " requests to enter room " + roomName);

        /* ===== ===== ===== Check user sanity ===== ===== ===== */
        logger.debug("Locating user entity");
        User locatedUser = userDao.findByIdentifier(null, null, uid);
        if (locatedUser == null)
            throw new UserNotFoundException("unknown");

        /* ===== ===== ===== Check room sanity ===== ===== ==== */
        logger.debug("Locating chatroom entity");
        Chatroom locatedRoom = chatroomDao.findRoomByName(roomName);
        if (locatedRoom == null)
            throw new ChatroomNotFoundException(roomName);

        /* ===== ===== ===== Verify JWT ===== ===== ===== */
        logger.debug("Verifying token");
        if (!jwtAuthService.verifyToken(token, publicKey, locatedUser))
            throw new TokenExpiredException("unknown");

        /* ===== ===== ===== Join room ===== ===== ===== */
        logger.debug("Registering user");
        chatroomDao.registerUserToRoom(locatedUser, locatedRoom);
        return "Successfully joined room";
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @GetMapping("/create-room")
    public String createChatroom(@RequestParam String newRoomName, @RequestParam Long uid,
                                 @RequestHeader String token, @RequestHeader byte[] publicKey) {
        /* ===== ===== ===== Check user sanity ===== ===== ===== */
        logger.debug("Locating user entity");
        User locatedUser = userDao.findByIdentifier(null, null, uid);
        if (locatedUser == null)
            throw new UserNotFoundException("unknown");

        /* ===== ===== ===== Check room sanity ===== ===== ==== */
        logger.debug("Locating chatroom entity");
        Chatroom locatedRoom = chatroomDao.findRoomByName(newRoomName);
        if (locatedRoom != null)
            throw new ChatRoomAlreadyExistsException(newRoomName);
        return "Successfully created room";
    }

}
