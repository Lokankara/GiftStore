package com.store.gift.repository;

import com.store.gift.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * <p>
 * This repository provides CRUD operations for User entities, as well as additional query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves a User entity by username.
     * This method retrieves a User entity from the repository based on the provided username.
     * It fetches the associated role, role authorities, and orders in a single query using entity graph.
     *
     * @param name the username to search for
     * @return an optional User entity
     */
    @EntityGraph(attributePaths = {"role", "role.authorities", "orders"})
    Optional<User> findByUsername(String name);

    /**
     * Retrieves all User entities with pagination.
     * This method retrieves all User entities from the repository with pagination support.
     * It fetches the associated orders, order certificates, order certificate tags, role, and role authorities
     * in a single query using entity graph.
     *
     * @param pageable the pagination information
     * @return a page of User entities
     */
    @NonNull
    @EntityGraph(attributePaths = {"orders", "orders.certificates", "orders.certificates.tags", "role", "role.authorities"})
    Page<User> findAll(@NonNull Pageable pageable);
}
