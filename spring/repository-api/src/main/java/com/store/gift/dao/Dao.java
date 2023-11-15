package com.store.gift.dao;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Generic interface for data access operations.
 *
 * @param <T> the entity type
 */
public interface Dao<T extends Serializable> {

    /**
     * Retrieves all entities.
     *
     * @param pageable the pagination information
     * @return a list of entities
     */
    List<T> getAllBy(Pageable pageable);

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity
     * @return an optional containing the entity,
     * or an empty optional if not found
     */
    Optional<T> getById(Long id);

    /**
     * Retrieves an entity by its name.
     *
     * @param name the name of the entity
     * @return an optional containing the entity,
     * or an empty optional if not found
     */
    Optional<T> findByUsername(String name);

    /**
     * Saves an entity.
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void delete(Long id);
}
