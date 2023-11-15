package com.store.gift.dao;

import com.store.gift.entity.Certificate;
import com.store.gift.entity.Criteria;
import com.store.gift.entity.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * This interface provides methods for accessing
 * and manipulating Certificates in the data source.
 */
public interface CertificateDao extends Dao<Certificate> {
    /**
     * Finds a certificate by its ID.
     *
     * @param id the ID of the certificate to find
     * @return the found certificate, or null if not found
     */
    Certificate findById(Long id);

    /**
     * Updates the specified certificate.
     *
     * @param certificate the certificate to update
     * @return the updated certificate
     */
    Certificate update(Certificate certificate);

    /**
     * Finds the tags associated with a certificate specified by ID.
     *
     * @param id the ID of the certificate
     * @return the list of tags associated with the certificate
     */
    List<Tag> findTagsByCertificateId(Long id);

    /**
     * Finds certificates that have any of the specified tag names.
     *
     * @param criteria the list of tag names to search for
     * @param pageable the page information
     * @return the list of certificates matching the tag names
     */
    List<Certificate> findByCriteria(Criteria criteria, Pageable pageable);

    /**
     * Retrieves all certificates associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return the list of certificates associated with the user
     */
    List<Certificate> getCertificatesByUserId(Long userId);

    /**
     * Retrieves a set of certificates by their IDs.
     *
     * @param certificateIds the set of certificate IDs
     * @return the set of certificates matching the IDs
     */
    List<Certificate> findAllByIds(Set<Long> certificateIds);

    /**
     * Retrieves all certificates associated with a specific order ID.
     *
     * @param id the ID of the order
     * @return the set of certificates associated with the order
     */
    Set<Certificate> findAllByOrderId(Long id);
}
