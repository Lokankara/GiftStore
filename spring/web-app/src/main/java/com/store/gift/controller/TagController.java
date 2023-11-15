package com.store.gift.controller;

import com.store.gift.assembler.TagAssembler;
import com.store.gift.dto.TagDto;
import com.store.gift.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Controller class for handling tag-related operations.
 * <p>
 * This class is annotated with {@link RequestMapping}
 * with a value of "/tags" to map requests to this controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {
    /**
     * The tag service for performing tag-related operations.
     */
    private final TagService tagService;

    /**
     * The tag assembler for converting tag entities to DTOs.
     */
    private final TagAssembler tagAssembler;

    /**
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag
     * @return the {@link EntityModel} of the tag DTO
     */
    @GetMapping(value = "/{id}")
    public EntityModel<TagDto> getById(@PathVariable final Long id) {
        return tagAssembler.toModel(tagService.getById(id));
    }

    /**
     * Retrieves all tags with pagination.
     *
     * @param pageable the pagination information
     * @return the {@link CollectionModel} of the tag DTOs
     */
    @GetMapping
    public CollectionModel<EntityModel<TagDto>> getAll(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return tagAssembler.toCollectionModel(tagService.getAll(pageable));
    }

    /**
     * Creates a new tag.
     *
     * @param tagDto the tag DTO to create
     * @return the created tag DTO
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public TagDto create(
            @RequestBody final TagDto tagDto) {
        return tagService.save(tagDto);
    }

    /**
     * Deletes a tag by its ID.
     *
     * @param id the ID of the tag to delete
     * @return the {@link ResponseEntity} with the HTTP status
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable final Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
