package com.store.gift.assembler;

import com.store.gift.controller.CertificateController;
import com.store.gift.dto.CertificateDto;
import com.store.gift.dto.PatchCertificateDto;
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
 * Component class for assembling representations of CertificateDto.
 */
@Component
public class CertificateAssembler
        implements RepresentationModelAssembler
        <CertificateDto, EntityModel<CertificateDto>> {
    /**
     * Converts a CertificateDto into an EntityModel representation.
     *
     * @param dto the CertificateDto to convert
     * @return the EntityModel representation of the CertificateDto
     */
    @NonNull
    @Override
    public EntityModel<CertificateDto> toModel(
            final CertificateDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CertificateController.class)
                        .getCertificateById(dto.getId())).withSelfRel(),
                linkTo(methodOn(CertificateController.class)
                        .getTagsByCertificateId(dto.getId())).withRel("tags"),
                linkTo(methodOn(CertificateController.class)
                        .delete(dto.getId())).withRel("delete"),
                linkTo(methodOn(CertificateController.class)
                        .create(dto)).withRel("create"),
                linkTo(methodOn(CertificateController.class)
                        .update(dto.getId(), PatchCertificateDto
                                .builder()
                                .duration(dto.getDuration())
                                .price(dto.getPrice())
                                .id(dto.getId())
                                .build()))
                        .withRel("update")
        );
    }

    /**
     * Converts a collection of CertificateDto
     * entities into a CollectionModel representation.
     *
     * @param entities the collection
     *                 of CertificateDto entities to convert
     * @return the CollectionModel representation
     * of the CertificateDto entities
     */
    @NonNull
    @Override
    public CollectionModel<EntityModel<CertificateDto>> toCollectionModel(
            final Iterable<? extends CertificateDto> entities) {
        return CollectionModel.of(StreamSupport
                        .stream(entities.spliterator(), false)
                        .map(this::toModel)
                        .toList(),
                linkTo(methodOn(CertificateController.class)
                        .getAll(PageRequest.of(0, 25,
                                Sort.by(Sort.Direction.ASC, "id"))))
                        .withSelfRel());
    }
}
