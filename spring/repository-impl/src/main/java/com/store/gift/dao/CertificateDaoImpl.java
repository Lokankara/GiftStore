package com.store.gift.dao;

import com.store.gift.entity.Certificate;
import com.store.gift.entity.Criteria;
import com.store.gift.entity.Order;
import com.store.gift.entity.Tag;
import com.store.gift.exception.CertificateNotFoundException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Subgraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toSet;


/**
 * The implementation of the CertificateDao interface.
 * <p>
 * This class provides the concrete implementation
 * for accessing and manipulating certificates in the database.
 * <p>
 * It utilizes the EntityManagerFactory and EntityManager
 * to interact with the database.
 */

@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {
    private static final String NOT_FOUND_WITH_ID = "Certificate not found with ID: ";
    /**
     * The entity manager factory used for obtaining the entity manager.
     */
    @PersistenceUnit
    private final EntityManagerFactory factory;

    /**
     * Retrieves a certificate by its ID.
     *
     * @param id the ID of the certificate
     * @return an optional containing the certificate if found,
     * or an empty optional if not found
     */
    @Override
    public Optional<Certificate> getById(final Long id) {
        return Optional.ofNullable(findById(id));
    }

    /**
     * Retrieves a certificate by its name.
     *
     * @param name the name of the certificate
     * @return an optional containing the certificate if found,
     * or an empty optional if not found
     */
    @Override
    public Optional<Certificate> findByUsername(final String name) {
        try (EntityManager entityManager = factory
                .createEntityManager()) {
            List<Certificate> certificates =
                    entityManager.createQuery(Queries.SELECT_BY_NAME, Certificate.class)
                            .setParameter(Queries.NAME, name)
                            .getResultList();
            return certificates.isEmpty()
                    ? Optional.empty()
                    : Optional.of(certificates.get(0));
        }
    }

    /**
     * Retrieves all certificates with pagination.
     *
     * @param pageable the pagination information
     * @return a list of certificates based
     * on the pagination parameters
     */
    @Override
    public List<Certificate> getAllBy(final Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager.createEntityGraph(
                    Certificate.class);
            return new ArrayList<>(findAllByIds(new HashSet<>(
                    entityManager.createQuery("SELECT c.id FROM Certificate c", Long.class)
                            .setHint(Queries.FETCH_GRAPH, graph)
                            .setFirstResult((int) pageable.getOffset())
                            .setMaxResults(pageable.getPageSize())
                            .getResultList())));
        }
    }

    /**
     * <p>Retrieves a certificate by its ID.</p>
     * <p>
     * This method fetches a certificate
     * from the database based on its ID.
     * <p>
     * It creates an entity graph to include the "tags"
     * attribute when retrieving the certificate.
     * <p>
     * The entity manager is used to find the certificate using
     * the specified ID and the entity graph hints.
     *
     * @param id the ID of the certificate
     * @return the certificate with the specified ID,
     * or null if not found
     */
    @Override
    public Certificate findById(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager.createEntityGraph(
                    Certificate.class);
            graph.addAttributeNodes(Queries.TAGS);
            Map<String, Object> hints = new HashMap<>();
            hints.put(Queries.FETCH_GRAPH, graph);
            return entityManager.find(Certificate.class, id, hints);
        }
    }

    /**
     * <p>Saves a certificate</p>
     * <p>
     * This method saves the specified certificate to the database.
     * </p>
     * The entity manager is used to perform the transaction
     * and persist the certificate object.
     *
     * @param certificate the certificate to be saved
     * @return the saved certificate
     */
    @Override
    public Certificate save(final Certificate certificate) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                setCertificateTags(entityManager, certificate);
                entityManager.persist(certificate);
                transaction.commit();
                return certificate;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e.getMessage());
            }
        }
    }

    /**
     * Removes the certificate with the specified ID from the database.
     * <p>
     * It uses the EntityManager and EntityTransaction
     * to perform the deletion in a transactional manner.
     * <p>
     * If any exception occurs during the process,
     * the transaction is rolled back
     * and a PersistenceException is thrown.
     *
     * @param id the ID of the certificate to be deleted
     * @throws PersistenceException if an error
     *                              occurs during the deletion process
     */
    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                if (getById(id).isEmpty()) {
                    throw new CertificateNotFoundException(
                            NOT_FOUND_WITH_ID + id);
                }
                entityManager.createNativeQuery(Queries.DELETE_ORDER_CERTIFICATE)
                        .setParameter(Queries.ID, id)
                        .executeUpdate();
                entityManager.createNativeQuery(Queries.DELETE_CERTIFICATE_TAG)
                        .setParameter(Queries.ID, id)
                        .executeUpdate();
                entityManager.createNativeQuery(Queries.DELETE_CERTIFICATE)
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
     * Updates a certificate in the database.
     * <p>
     * The method updates a certificate in the database.
     * It mentions the usage of EntityManager and EntityTransaction
     * for performing the update in a transactional manner.
     * <p>
     * The method checks if the certificate exists by its ID and throws
     * a CertificateNotFoundException if it doesn't.
     * It then updates the fields of the existing certificate based
     * on the provided certificate object.
     *
     * @param certificate the certificate object with updated values
     * @return the updated certificate
     * @throws CertificateNotFoundException if the certificate
     *                                      does not exist in the database
     * @throws PersistenceException         if an error occurs
     *                                      during the update process
     */
    @Override
    public Certificate update(
            final Certificate certificate) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityTransaction transaction =
                    entityManager.getTransaction();
            try {
                transaction.begin();
                EntityGraph<Certificate> graph = entityManager
                        .createEntityGraph(Certificate.class);
                graph.addAttributeNodes(Queries.ORDERS, Queries.TAGS);
                Subgraph<Order> orderSubgraph = graph.addSubgraph(Queries.ORDERS);
                orderSubgraph.addAttributeNodes(Queries.USER);

                Certificate existed = getById(certificate.getId())
                        .orElseThrow(() -> new CertificateNotFoundException(
                                NOT_FOUND_WITH_ID + certificate.getId()));

                if (certificate.getPrice() != null) {
                    existed.setPrice(certificate.getPrice());
                }
                if (certificate.getDuration() != null) {
                    existed.setDuration(certificate.getDuration());
                }
                if (certificate.getTags() != null) {
                    setCertificateTags(entityManager,
                            certificate,
                            existed);
                }
                transaction.commit();
                return existed;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e.getMessage());
            }
        }
    }

    /**
     * Retrieves the tags associated with a certificate by its ID.
     * <p>
     * This method retrieves the tags associated with
     * a certificate from the database based on its ID.
     *
     * @param id the ID of the certificate
     * @return a list of tags associated with the certificate
     */
    @Override
    public List<Tag> findTagsByCertificateId(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return entityManager
                    .createQuery(Queries.SELECT_TAGS_BY_ID, Tag.class)
                    .setParameter(Queries.ID, id)
                    .getResultList();
        }
    }

    /**
     * Retrieves certificates based on a list of tag names.
     * <p>
     * This method retrieves certificates from the database
     * that are associated with any of the specified tag names.
     * <p>
     * It uses the EntityManager to create a query that selects
     * the certificates based on the provided tag names.
     * <p>
     * The method sets the tag names as parameters in the query
     * and returns the resulting list of certificates.
     *
     * @param criteria the list of tag names
     * @return a list of certificates associated
     * with any of the specified tag names
     */
    @Override
    public List<Certificate> findByCriteria(
            final Criteria criteria,
            final Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Certificate> query =
                    builder.createQuery(Certificate.class);
            Root<Certificate> root = query.from(Certificate.class);
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getTagNames() != null && !criteria.getTagNames().isEmpty()) {
                Join<Certificate, Tag> tagJoin = root.join(Queries.TAGS, JoinType.INNER);
                predicates.add(tagJoin.get(Queries.NAME).in(criteria.getTagNames()));
            }

            if (criteria.getName() != null) {
                predicates.add(builder.like(root.get(Queries.NAME),
                        String.format("%%%s%%", criteria.getName())));
            }
            if (criteria.getDescription() != null) {
                predicates.add(builder.like(root.get(Queries.DESCRIPTION),
                        String.format("%%%s%%", criteria.getDescription())));
            }

            query.select(root).where(builder.and(predicates.toArray(new Predicate[0])));

            if (pageable.getSort().isSorted()) {
                List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();
                for (Sort.Order order : pageable.getSort()) {
                    orders.add(order.getDirection().equals(Sort.Direction.ASC)
                            ? builder.asc(root.get(order.getProperty()))
                            : builder.desc(root.get(order.getProperty())));
                }
                query.orderBy(orders);
            }

            EntityGraph<Certificate> graph = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes(Queries.TAGS);

            return entityManager.createQuery(query)
                    .setHint(Queries.FETCH_GRAPH, graph)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    /**
     * Retrieves the certificates associated with a user by their ID.
     * <p>
     * This method retrieves the certificates associated with
     * a user from the database based on their ID.
     *
     * @param id the ID of the user
     * @return a list of certificates associated with the user
     */
    @Override
    public List<Certificate> getCertificatesByUserId(
            final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityGraph<Certificate> graph =
                    entityManager.createEntityGraph(Certificate.class);
            graph.addAttributeNodes(Queries.TAGS);
            return entityManager.createQuery(
                            Queries.SELECT_CERTIFICATES_BY_USER_ID,
                            Certificate.class)
                    .setParameter(Queries.ID, id)
                    .setHint(Queries.FETCH_GRAPH, graph)
                    .getResultList();
        }
    }

    /**
     * Retrieves the certificates based on a set of certificate IDs.
     * <p>
     * This method retrieves the certificates from
     * the database that match the IDs specified in the set.
     * <p>
     * The method sets the IDs parameter
     * in the query and returns the resulting set of certificates.
     *
     * @param certificateIds the set of certificate IDs
     * @return a set of certificates matching the specified IDs
     */
    public List<Certificate> findAllByIds(
            final Set<Long> certificateIds) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes(Queries.TAGS);
            return entityManager.createQuery(
                            Queries.SELECT_ALL_BY_IDS,
                            Certificate.class)
                    .setParameter("ids", certificateIds)
                    .setHint(Queries.FETCH_GRAPH, graph)
                    .getResultList();
        }
    }

    /**
     * Retrieves the certificates associated with an order by its ID.
     * <p>
     * This method retrieves the certificates associated with
     * an order from the database based on its ID.
     * <p>
     * The method sets the order ID parameter in the query
     * and returns the resulting set of certificates.
     *
     * @param orderId the ID of the order
     * @return a set of certificates associated with the order
     */
    public Set<Certificate> findAllByOrderId(final Long orderId) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager.createEntityGraph(
                    Certificate.class);
            graph.addAttributeNodes(Queries.TAGS);
            graph.addSubgraph(Queries.ORDERS).addAttributeNodes(Queries.USER);

            List<Certificate> list = entityManager.createQuery(
                            Queries.SELECT_CERTIFICATES_BY_ORDER_ID,
                            Certificate.class)
                    .setParameter("orderId", orderId)
                    .setHint(Queries.FETCH_GRAPH, graph)
                    .getResultList();
            return new HashSet<>(list);
        }
    }

    /**
     * Sets the tags for a certificate using the provided EntityManager.
     * <p>
     * It maps the existing tags of the certificate
     * to the corresponding tags fetched from the database based on their names.
     * The method updates the certificate's tags with the fetched tags.
     *
     * @param entityManager the EntityManager used for database access
     * @param certificate   the certificate for which to set the tags
     */
    private void setCertificateTags(
            final EntityManager entityManager,
            final Certificate certificate) {
        Set<Tag> tagSet = certificate.getTags()
                .stream()
                .map(tag -> entityManager.createQuery(
                                Queries.SELECT_TAGS_BY_NAME,
                                Tag.class)
                        .setParameter(Queries.NAME, tag.getName())
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(tag))
                .collect(toSet());
        certificate.setTags(tagSet);
    }

    private void setCertificateTags(
            final EntityManager entityManager,
            final Certificate certificate,
            final Certificate existed) {
        Set<Tag> updatedTags = existed.getTags();
        List<String> tagNames = certificate.getTags()
                .stream()
                .map(Tag::getName)
                .toList();
        List<Tag> existingTags = entityManager.createQuery(
                        Queries.SELECT_TAG_BY_NAMES,
                        Tag.class)
                .setParameter("names", tagNames)
                .getResultList();
        Map<String, Tag> existingTagsMap = existingTags.stream()
                .collect(Collectors.toMap(Tag::getName, identity()));
        for (Tag tag : certificate.getTags()) {
            Tag existingTag = existingTagsMap.get(tag.getName());
            if (existingTag != null) {
                updatedTags.add(existingTag);
            } else {
                entityManager.persist(tag);
                updatedTags.add(tag);
            }
        }
        existed.setTags(updatedTags);
    }
}
