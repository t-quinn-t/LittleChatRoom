package com.server.model_assembler;

import com.server.model.Chatroom;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class ChatroomModelAssembler implements RepresentationModelAssembler<Chatroom, EntityModel<Chatroom>> {

    @Override
    public EntityModel<Chatroom> toModel(Chatroom entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<Chatroom>> toCollectionModel(Iterable<? extends Chatroom> entities) {
        Collection<EntityModel<Chatroom>> chatroomList = new ArrayList<>();
        for (Chatroom chatroom: entities) {
            chatroomList.add(EntityModel.of(chatroom));
        }
        return CollectionModel.of(chatroomList);
    }
}
