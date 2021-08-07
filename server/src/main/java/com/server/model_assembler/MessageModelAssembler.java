package com.server.model_assembler;

import com.server.model.Message;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Qintu (Quinn) Tao
 * @date: 2021-08-02 4:08 p.m.
 */

@Component
public class MessageModelAssembler implements RepresentationModelAssembler<Message, EntityModel<Message>> {
    @Override
    public EntityModel<Message> toModel(@NonNull Message entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<Message>> toCollectionModel(Iterable<? extends Message> entities) {
        Collection<EntityModel<Message>> messageList = new ArrayList<>();
        for (Message message: entities) {
            messageList.add(EntityModel.of(message));
        }
        return CollectionModel.of(messageList);
    }
}
