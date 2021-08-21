package com.server.controller;

import com.server.dao.ChatroomDao;
import com.server.exception.ChatroomNotFoundException;
import com.server.model.Chatroom;
import com.server.model_assembler.ChatroomModelAssembler;
import com.server.service.JWTAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:3000"})
@RestController
@RequestMapping("/chatroom")
public class ChatroomController {

    private final ChatroomDao chatroomDao;
    private final ChatroomModelAssembler chatroomModelAssembler;
    private final JWTAuthService jwtAuthService;
    private final Logger logger = LoggerFactory.getLogger(ChatroomController.class);

    @Autowired
    public ChatroomController(ChatroomDao chatroomDao, ChatroomModelAssembler chatroomModelAssembler,
                               @Qualifier("jwtservice") JWTAuthService jwtAuthService) {
        this.chatroomDao = chatroomDao;
        this.chatroomModelAssembler = chatroomModelAssembler;
        this.jwtAuthService = jwtAuthService;
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

}
