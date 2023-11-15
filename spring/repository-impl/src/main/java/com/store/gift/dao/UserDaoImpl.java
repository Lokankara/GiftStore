package com.store.gift.dao;


import com.store.gift.entity.Order;
import com.store.gift.entity.Role;
import com.store.gift.entity.RoleType;
import com.store.gift.entity.User;
import com.store.gift.exception.RoleNotFoundException;
import com.store.gift.exception.UserAlreadyExistsException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Subgraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The implementation of the UserDao interface.
 * <p>
 * This class provides the concrete implementation
 * for accessing and manipulating users in the database.
 * <p>
 * It utilizes the EntityManagerFactory and EntityManager
 * to interact with the database.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    /**
     * The entity manager factory used for obtaining the entity manager.
     */
    @PersistenceUnit
    private final EntityManagerFactory factory;

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all users with pagination.
     *
     * @param pageable the pagination information
     * @return a list of user entities
     */
    @Override
    public List<User> getAllBy(final Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);

            EntityGraph<User> graph = getUserEntityGraph(entityManager);
            query.select(root);

            if (pageable.getSort().isSorted()) {
                List<jakarta.persistence.criteria.Order> orders = pageable.getSort()
                        .stream()
                        .map(order -> order.getDirection().equals(Sort.Direction.ASC)
                                ? builder.asc(root.get(order.getProperty()))
                                : builder.desc(root.get(order.getProperty())))
                        .toList();
                query.orderBy(orders);
            }

            return entityManager.createQuery(query)
                    .setHint(Queries.FETCH_GRAPH, graph)
                    .setFirstResult(pageable.getPageNumber()
                            * pageable.getPageSize())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return an {@link Optional} containing the user entity,
     * or empty if not found
     */
    public Optional<User> getById(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
            graph.addSubgraph("role");
            graph.addSubgraph("orders");
            Map<String, Object> hints = new HashMap<>();
            hints.put(Queries.FETCH_GRAPH, graph);
            User user = entityManager.find(User.class, id, hints);
            return Optional.ofNullable(user);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a user by its name.
     *
     * @param name the name of the user
     * @return an {@link Optional} containing the user entity,
     * or empty if not found
     */
    @Override
    public Optional<User> findByUsername(final String name) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {

            List<User> users = entityManager
                    .createQuery(Queries.SELECT_USER_BY_NAME, User.class)
                    .setParameter(Queries.NAME, name)
                    .getResultList();
            return users.isEmpty()
                    ? Optional.empty()
                    : Optional.of(users.get(0));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Saves a user.
     *
     * @param user the user to save
     * @return the saved user entity
     * @throws UserAlreadyExistsException if the user already exists
     */
    @Override
    public User save(final User user) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                boolean empty = entityManager
                        .createQuery(Queries.SELECT_USER_BY_NAME, User.class)
                        .setParameter(Queries.NAME, user.getUsername())
                        .getResultList().isEmpty();
                if (!empty) {
                    throw new UserAlreadyExistsException("User is Already Exists");
                }
                entityManager.persist(user);
                transaction.commit();
                return user;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e.getMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete
     * @throws PersistenceException if an error occurs during the deletion
     */
    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.createNativeQuery(Queries.DELETE_ORDER)
                        .setParameter("id", id)
                        .executeUpdate();

                entityManager.createNativeQuery(Queries.DELETE_TOKEN)
                        .setParameter("id", id)
                        .executeUpdate();
                entityManager.createNativeQuery(Queries.DELETE_USER)
                        .setParameter("id", id)
                        .executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param user the entity of the user to update
     * @throws PersistenceException if an error occurs during the deletion
     */
    @Override
    public User update(final User user) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            try {
                CriteriaBuilder builder = entityManager.getCriteriaBuilder();
                CriteriaUpdate<User> update = builder.createCriteriaUpdate(User.class);
                Root<User> root = update.from(User.class);

                if (user.getUsername() != null) {
                    update.set(root.get("username"), user.getUsername());
                }
                if (user.getEmail() != null) {
                    update.set(root.get("email"), user.getEmail());
                }
                if (user.getPassword() != null) {
                    update.set(root.get("password"), user.getPassword());
                }
                if (user.getRole() != null && user.getRole().getPermission() != null) {
                    Role existingRole = findRoleByPermission(
                            entityManager, user.getRole().getPermission())
                            .orElseThrow(() -> new RoleNotFoundException(
                                    "Role not found with permission "
                                            + user.getRole().getPermission()));
                    user.setRole(existingRole);
                    update.set(root.get("role"), user.getRole());
                }

                update.where(builder.equal(root.get("id"), user.getId()));

                entityManager.createQuery(update)
                        .setHint(Queries.FETCH_GRAPH, getUserEntityGraph(entityManager))
                        .executeUpdate();
                transaction.commit();

                return entityManager.find(User.class, user.getId());
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e);
            }
        }
    }

    /**
     * Retrieves a role based on the given permission.
     *
     * @param entityManager the to build query by CriteriaBuilder
     * @param permission the permission to search for
     * @return an optional containing the role if found, or empty if not found
     */
    public Optional<Role> findRoleByPermission(
            final EntityManager entityManager,
            final RoleType permission) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> query = builder.createQuery(Role.class);
        query.where(builder.equal(query.from(Role.class).get("permission"), permission));
        List<Role> roles = entityManager
                .createQuery(query)
                .setMaxResults(1)
                .getResultList();
        return roles.isEmpty()
                ? Optional.empty()
                : Optional.of(roles.get(0));
    }

    /**
     * Retrieves the entity graph for the User entity.
     * This method creates and configures an entity graph for the User entity.
     *
     * @param entityManager the entity manager
     * @return the entity graph for the User entity
     */
    private static EntityGraph<User> getUserEntityGraph(
            EntityManager entityManager) {
        EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
        graph.addAttributeNodes(Queries.ORDERS, "role", "tokens");
        Subgraph<Order> orderSubgraph = graph.addSubgraph(Queries.ORDERS);
        orderSubgraph.addAttributeNodes(Queries.CERTIFICATES);
        orderSubgraph.addSubgraph(Queries.CERTIFICATES).addAttributeNodes(Queries.TAGS);
        graph.addSubgraph("role").addAttributeNodes("authorities");
        return graph;
    }
}
