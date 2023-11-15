package com.store.gift.assembler;

import com.store.gift.controller.CertificateController;
import com.store.gift.controller.OrderController;
import com.store.gift.controller.UserController;
import com.store.gift.dto.UserDto;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * An implementation of the RepresentationModelAssembler interface
 * that converts entities of type {@link UserDto} into {@link EntityModel<UserDto>}.
 */
@Component
public class UserAssembler implements
        RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {
    /**
     * Converts the given {@link UserDto} entity into an {@link EntityModel<UserDto>}.
     *
     * @param userDto the {@link UserDto} entity to be converted
     * @return the {@link EntityModel<UserDto>} representing the converted entity
     */
    @NonNull
    @Override
    public EntityModel<UserDto> toModel(
            final UserDto userDto) {
        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class)
                        .getUser(userDto.getId()))
                        .withSelfRel(),
                linkTo(methodOn(OrderController.class)
                        .getAllOrdersByUserId(userDto.getId(),
                                PageRequest.of(0, 25, Sort.by("name").ascending())))
                        .withRel("orders"),
                linkTo(methodOn(CertificateController.class)
                        .getUserCertificates(userDto.getId()))
                        .withRel("certificates")
        );
    }

    /**
     * Converts an {@link Iterable} of {@link UserDto} entities
     * into a {@link CollectionModel} of {@link EntityModel}.
     *
     * @param users the {@link UserDto} entities to be converted
     * @return the {@link CollectionModel}
     * of {@link EntityModel<UserDto>} representing the converted entities
     */
    @NonNull
    @Override
    public CollectionModel<EntityModel<UserDto>> toCollectionModel(
            final Iterable<? extends UserDto> users) {
        return CollectionModel.of(StreamSupport
                        .stream(users.spliterator(), false)
                        .map(this::toModel)
                        .toList(),
                linkTo(methodOn(UserController.class)
                        .getUsers(PageRequest.of(
                                0, 25,
                                Sort.by("name").ascending())))
                        .withSelfRel());
    }
}
