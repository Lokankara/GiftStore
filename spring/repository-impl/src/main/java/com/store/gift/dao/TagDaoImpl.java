package com.store.gift.dao;

import com.store.gift.entity.Tag;
import com.store.gift.exception.CertificateNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * The implementation of the TagDao interface.
 * <p>
 * This class provides the concrete implementation
 * for accessing and manipulating tags in the database.
 * <p>
 * It utilizes the EntityManagerFactory and EntityManager
 * to interact with the database.
 */
@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {
    /**
     * The error message indicating that a tag was not found with the specified ID.
     */
    public static final String TAG_NOT_FOUND_WITH_ID = "Tag not found with ID: ";
    /**
     * The entity manager factory used for obtaining the entity manager.
     */
    @PersistenceUnit
    private final EntityManagerFactory factory;

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag
     * @return an {@link Optional} containing the tag entity, or empty if not found
     */
    @Override
    public Optional<Tag> getById(final Long id) {
        try {
            return Optional.ofNullable(findById(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a tag by its name.
     *
     * @param name the name of the tag
     * @return an {@link Optional} containing the tag entity, or empty if not found
     */
    @Override
    public Optional<Tag> findByUsername(
            final String name) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
            query.where(builder.equal(query.from(Tag.class).get(Queries.NAME), name));
            List<Tag> tags = entityManager
                    .createQuery(query)
                    .setMaxResults(1)
                    .getResultList();
            return tags.isEmpty()
                    ? Optional.empty()
                    : Optional.of(tags.get(0));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all tags with pagination.
     *
     * @param pageable the pagination information
     * @return a list of tag entities
     */
    @Override
    public List<Tag> getAllBy(final Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
            Root<Tag> root = query.from(Tag.class);
            query.select(root);

            if (pageable.getSort().isSorted()) {
                List<Order> orders = pageable.getSort().stream()
                        .map(order -> order.getDirection().equals(Sort.Direction.ASC)
                                ? builder.asc(root.get(order.getProperty()))
                                : builder.desc(root.get(order.getProperty())))
                        .toList();
                query.orderBy(orders);
            }

            return entityManager.createQuery(query)
                    .setMaxResults(pageable.getPageSize())
                    .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                    .getResultList();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Saves a tag.
     *
     * @param tag the tag to save
     * @return the saved tag entity
     */
    @Override
    public Tag save(final Tag tag) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityTransaction transaction =
                    entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.persist(tag);
                transaction.commit();
                return tag;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e.getMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Finds a tag by its ID.
     *
     * @param id the ID of the tag
     * @return the found tag entity, or null if not found
     */
    @Override
    public Tag findById(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return entityManager.find(Tag.class, id);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deletes a tag by its ID.
     *
     * @param id the ID of the tag to delete
     * @throws PersistenceException if the tag is not found
     */
    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                if (entityManager.find(Tag.class, id) == null) {
                    throw new CertificateNotFoundException(
                            TAG_NOT_FOUND_WITH_ID + id);
                }
                entityManager.createNativeQuery(Queries.DELETE_CT_BY_TAG_ID)
                        .setParameter(Queries.ID, id)
                        .executeUpdate();
                entityManager.createQuery(Queries.DELETE_TAG)
                        .setParameter(Queries.ID, id)
                        .executeUpdate();
                transaction.commit();
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
     * Saves a set of tags.
     *
     * @param tags the set of tags to save
     * @return the saved tags
     */
    @Override
    public Set<Tag> saveAll(
            final Set<Tag> tags) {
        return tags.stream()
                .map(this::save)
                .collect(toSet());
    }
}
