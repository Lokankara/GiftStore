package com.store.gift.service;

import com.store.gift.dto.UserDto;
import com.store.gift.dto.UserSlimDto;
import com.store.gift.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Find a user by ID.
     *
     * @param id the user ID
     * @return the user entity
     */
    User findById(Long id);

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     */
    UserDto getById(Long id);

    /**
     * Get all users with pagination.
     *
     * @param pageable the pageable information
     * @return the page of user DTOs
     */
    Page<UserDto> getAll(Pageable pageable);

    UserDto save(UserSlimDto dto);

    UserDto update(UserSlimDto dto);

    void delete(Long id);
}
