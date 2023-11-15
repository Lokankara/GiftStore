package com.store.gift.repository.impl;

import com.store.gift.dao.UserDao;
import com.store.gift.entity.User;
import com.store.gift.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toCollection;

@RequiredArgsConstructor
public class UserRepoImpl implements UserDao {

    private final UserRepository userRepository;

    /**
     * Retrieves all entities.
     *
     * @param pageable the pagination information
     * @return a list of entities
     */
    @Override
    public List<User> getAllBy(final Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .collect(toCollection(ArrayList::new));
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity
     * @return an optional containing the entity,
     * or an empty optional if not found
     */
    @Override
    public Optional<User> getById(final Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves an entity by its name.
     *
     * @param name the name of the entity
     * @return an optional containing the entity,
     * or an empty optional if not found
     */
    @Override
    public Optional<User> findByUsername(final String name) {
         return Optional.ofNullable(
                 userRepository.findByUsername(name)
                         .orElseThrow(()-> new UsernameNotFoundException(name)));
    }

    /**
     * Saves a User entity.
     * <p>
     * This method saves the provided User entity to the repository.
     *
     * @param user the User entity to be saved
     * @return the saved User entity
     */
    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

    /**
     * Updates a User entity.
     * <p>
     * This method updates the provided User entity.
     *
     * @param user the User entity to be updated
     * @return the updated User entity
     */
    @Override
    public User update(final User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a User entity by ID.
     * <p>
     * This method deletes the User entity with the specified ID from the repository.
     *
     * @param id the ID of the User entity to be deleted
     */
    @Override
    public void delete(final Long id) {
        userRepository.deleteById(id);
    }
}
