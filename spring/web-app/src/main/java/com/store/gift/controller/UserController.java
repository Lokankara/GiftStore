package com.store.gift.controller;

import com.store.gift.assembler.UserAssembler;
import com.store.gift.dto.UserDto;
import com.store.gift.dto.UserSlimDto;
import com.store.gift.service.UserService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Controller class for managing users.
 * <p>
 * Provides REST endpoints to retrieve user information.
 * <p>
 * This class is annotated with {@link RestController}
 * to indicate that it is a Spring MVC controller,
 * and {@link RequestMapping} with a path of "/users"
 * to map requests to this controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    /**
     * The User Assembler used to convert User entities to User DTOs.
     */
    private final UserAssembler assembler;
    /**
     * The User Service used to retrieve and manipulate user data.
     */
    private final UserService userService;

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     */
    @GetMapping("/{id}")
    public EntityModel<UserDto> getUser(
            @PathVariable final Long id) {
        return assembler.toModel(
                userService.getById(id));
    }

    /**
     * Get all users with pagination.
     *
     * @param pageable the pageable information
     * @return the collection of user DTOs
     */
    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getUsers(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                userService.getAll(pageable));
    }

    /**
     * Creates a new user.
     *
     * @param dto the user data
     * @return the created user DTO
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public UserDto create(
            @Valid @RequestBody final UserSlimDto dto) {
        return userService.save(dto);
    }

    /**
     * Updates an existing user.
     *
     * @param id  the ID of the user to update
     * @param dto the updated user data
     * @return the updated user DTO
     */
    @PatchMapping(value = "/{id}")
    public EntityModel<UserDto> update(
            @Valid @PathVariable final Long id,
            @Valid @RequestBody final UserSlimDto dto) {
        dto.setId(id);
        return assembler.toModel(userService.update(dto));
    }

    /**
     * Deletes a user.
     *
     * @param id the ID of the user to delete
     * @return the HTTP response status
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable final Long id) {
        userService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
