package com.store.gift.assembler;

import com.store.gift.controller.TagController;
import com.store.gift.dto.TagDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembles TagDto entities into EntityModels.
 * This class converts entities
 * of type {@link TagDto} into {@link EntityModel<TagDto>}.
 */
@Component
public class TagAssembler implements
        RepresentationModelAssembler<TagDto, EntityModel<TagDto>> {
    /**
     * Converts the given {@link TagDto} entity into an {@link EntityModel<TagDto>}.
     *
     * @param dto the {@link TagDto} entity to be converted
     * @return the {@link EntityModel<TagDto>} representing the converted entity
     */
    @NonNull
    @Override
    public EntityModel<TagDto> toModel(@NonNull final TagDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(TagController.class).getById(dto.getId())).withSelfRel(),
                linkTo(methodOn(TagController.class).delete(dto.getId())).withRel("delete")
        );
    }

    /**
     * Converts an {@link Iterable} of {@link TagDto} entities
     * into a {@link CollectionModel} of {@link EntityModel}.
     *
     * @param entities the {@link TagDto} entities to be converted
     * @return the {@link CollectionModel}
     * of {@link EntityModel<TagDto>} representing the converted entities
     */
    @NonNull
    @Override
    public CollectionModel<EntityModel<TagDto>> toCollectionModel(
            final Iterable<? extends TagDto> entities) {
        return CollectionModel.of(StreamSupport
                        .stream(entities.spliterator(), false)
                        .map(this::toModel)
                        .toList(),
                linkTo(methodOn(TagController.class)
                        .getAll(PageRequest.of(
                                0, 25,
                                Sort.by("id").ascending())))
                        .withSelfRel());
    }
}
