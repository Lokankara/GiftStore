package com.store.gift.service;

import com.store.gift.dao.UserDao;
import com.store.gift.dto.UserDto;
import com.store.gift.dto.UserSlimDto;
import com.store.gift.entity.User;
import com.store.gift.exception.UserNotFoundException;
import com.store.gift.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the UserService interface.
 * <p>
 * Provides methods for managing user data.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper mapper;

    /**
     * Get a user DTO by ID.
     *
     * @param id the user ID
     * @return the user DTO
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getById(final Long id) {
        return mapper.toDto(findById(id));
    }

    /**
     * Get a user entity by ID.
     *
     * @param id the user ID
     * @return the user entity
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public User findById(final Long id) {
        return userDao.getById(id).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User not found with id %d", id)));
    }

    /**
     * Get all users with pagination.
     *
     * @param pageable the pageable information
     * @return the page of user DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAll(final Pageable pageable) {
        List<UserDto> dtos = mapper.toDtoList(
                userDao.getAllBy(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    /**
     * Saves a new user.
     *
     * @param dto The UserDto object containing the user information to save.
     * @return The UserDto object of the saved user.
     */
    @Override
    @Transactional
    public UserDto save(
            final UserSlimDto dto) {
        User user = userDao.save(
                mapper.toEntity(dto));
        return mapper.toDto(user);
    }

    /**
     * Updates an existing user.
     *
     * @param dto The UserSlimDto object containing the updated user information.
     * @return The UserDto object of the updated user.
     */
    @Override
    @Transactional
    public UserDto update(
            final UserSlimDto dto) {
        User user = userDao.update(
                mapper.toEntity(dto));
        return mapper.toDto(user);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     */
    @Override
    @Transactional
    public void delete(
            final Long id) {
        userDao.delete(id);
    }
}
