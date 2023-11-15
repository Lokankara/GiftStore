package com.store.gift.service;

import com.store.gift.dto.CertificateDto;
import com.store.gift.dto.PatchCertificateDto;
import com.store.gift.dto.TagDto;
import com.store.gift.entity.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Service interface for managing certificates.
 */
public interface CertificateService {

    /**
     * Retrieves a certificate by its ID.
     *
     * @param id the ID of the certificate.
     * @return the retrieved certificate.
     */
    CertificateDto getById(Long id);

    /**
     * Retrieves all certificates with pagination.
     *
     * @param pageable the pagination information.
     * @return a List of certificates.
     */
    List<CertificateDto> getCertificates(Pageable pageable);

    /**
     * Retrieves a certificate by its name.
     *
     * @param name the name of the certificate.
     * @return the retrieved certificate.
     */
    CertificateDto getByName(String name);

    /**
     * Deletes a certificate by its ID.
     *
     * @param id the ID of the certificate to delete.
     */
    void delete(Long id);

    /**
     * Updates a certificate.
     *
     * @param dto the certificate DTO containing the updated information.
     * @return the updated certificate.
     */
    CertificateDto update(PatchCertificateDto dto);

    /**
     * Saves a new certificate with slim information.
     *
     * @param dto the certificate DTO containing the information to save.
     * @return the saved certificate.
     */
    CertificateDto save(CertificateDto dto);

    /**
     * Retrieves the tags associated with a certificate.
     *
     * @param id the ID of the certificate.
     * @return a set of tag DTOs associated with the certificate.
     */
    Set<TagDto> findTagsByCertificateId(Long id);

    /**
     * Retrieves all certificates associated with a list of tag names.
     *
     * @param criteria the list of tag names.
     * @param pageable the page
     * @return a page of certificates associated with the tags.
     */
    List<CertificateDto> findAllBy(Criteria criteria, Pageable pageable);

    /**
     * Retrieves all certificates associated with a user by user ID.
     *
     * @param userId the ID of the user.
     * @return a page of certificates associated with the user.
     */
    Page<CertificateDto> getCertificatesByUserId(Long userId);

    /**
     * Retrieves certificates by their IDs.
     *
     * @param ids the set of certificate IDs.
     * @return a list of certificates matching the IDs.
     */
    List<CertificateDto> getByIds(Set<Long> ids);

    /**
     * Retrieves certificates associated with an order by order ID.
     *
     * @param id the ID of the order.
     * @return a list of certificates associated with the order.
     */
    List<CertificateDto> getByOrderId(Long id);
}
