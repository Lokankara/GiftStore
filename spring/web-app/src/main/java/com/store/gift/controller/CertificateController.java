package com.store.gift.controller;

import com.store.gift.assembler.CertificateAssembler;
import com.store.gift.assembler.TagAssembler;
import com.store.gift.dto.CertificateDto;
import com.store.gift.dto.PatchCertificateDto;
import com.store.gift.dto.TagDto;
import com.store.gift.entity.Criteria;
import com.store.gift.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Controller class for handling certificate-related operations.
 * {@link RestController} to indicate that it is a Spring MVC controller,
 * {@link RequestMapping} with a value of "/certificates" to map request
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://192.168.31.177:4200",
        "http://localhost:5500",
        "http://localhost:4200",
        "http://127.0.0.1:5500",
        "http://127.0.0.1:8080",
        "http://127.0.0.1:4200",
        "https://gift-store-angular.netlify.app",
        "https://gift-store-certificate.netlify.app",
        "https://gift-store.onrender.com"})
@RequestMapping(value = "/certificates")
public class CertificateController {
    /**
     * The Certificate assembler for converting Certificate entities to DTOs.
     */
    private final CertificateAssembler assembler;
    /**
     * The tag assembler for converting tag entities to DTOs.
     */
    private final TagAssembler tagAssembler;
    /**
     * The Certificate service for performing tag-related operations.
     */
    private final CertificateService certificateService;

    /**
     * Retrieves a certificate by its ID.
     *
     * @param id the ID of the certificate
     * @return the EntityModel representation of the certificate
     */
    @GetMapping("/{id}")
    public EntityModel<CertificateDto> getCertificateById(
            @Valid @PathVariable final Long id) {
        return assembler.toModel(
                certificateService.getById(id));
    }

    /**
     * Retrieves all certificates.
     *
     * @param pageable the pageable information for pagination and sorting
     * @return the CollectionModel representation of all slim certificates
     */
    @GetMapping
    public CollectionModel<EntityModel<CertificateDto>> getAll(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                certificateService.getCertificates(pageable));
    }

    /**
     * Searches for certificates based on the provided criteria.
     *
     * @param name        the name of the certificate (optional)
     * @param description the description of the certificate (optional)
     * @param tagNames    the list of tag names associated with the certificate (optional)
     * @param pageable    the pagination information
     * @return the collection of certificate DTOs matching the search criteria
     */
    @GetMapping(value = "/search")
    public CollectionModel<EntityModel<CertificateDto>> search(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final String description,
            @RequestParam(required = false) final List<String> tagNames,
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                certificateService.findAllBy(
                        Criteria.builder()
                                .name(name)
                                .description(description)
                                .tagNames(tagNames).build(),
                        pageable));
    }

    /**
     * Updates a certificate.
     *
     * @param id  the ID of the certificate to update
     * @param dto the updated certificate data
     * @return the EntityModel representation of the updated certificate
     */
    @PatchMapping(value = "/{id}")
    public EntityModel<CertificateDto> update(
            @Valid @PathVariable final Long id,
            @Valid @RequestBody final PatchCertificateDto dto) {
        dto.setId(id);
        return assembler.toModel(certificateService.update(dto));
    }

    /**
     * Creates a new certificate.
     *
     * @param dto the data of the certificate to create
     * @return the created certificate
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public CertificateDto create(
            @Valid @RequestBody final CertificateDto dto) {
        return certificateService.save(dto);
    }

    /**
     * Deletes a certificate by its ID.
     *
     * @param id the ID of the certificate to delete
     * @return the HTTP status response
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable final Long id) {
        certificateService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * Retrieves the tags associated with a certificate.
     *
     * @param id the ID of the certificate
     * @return the CollectionModel representation of the tags
     */
    @GetMapping(value = "/{id}/tags")
    public CollectionModel<EntityModel<TagDto>> getTagsByCertificateId(
            @PathVariable final Long id) {
        return tagAssembler.toCollectionModel(
                certificateService.findTagsByCertificateId(id));
    }

    /**
     * Retrieves the certificates associated with a user.
     *
     * @param userId the ID of the user
     * @return the CollectionModel representation of the certificates
     */
    @GetMapping(value = "/users/{userId}")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public CollectionModel<EntityModel<CertificateDto>> getUserCertificates(
            @PathVariable final Long userId) {
        return assembler.toCollectionModel(
                certificateService.getCertificatesByUserId(userId));
    }

    /**
     * Retrieves all certificates associated with an order.
     *
     * @param orderId the ID of the order
     * @return the CollectionModel representation of the certificates
     */
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    @GetMapping(value = "/orders/{orderId}")
    public CollectionModel<EntityModel<CertificateDto>> getAllByOrderId(
            @PathVariable final Long orderId) {
        return assembler.toCollectionModel(
                certificateService.getByOrderId(orderId));
    }
}
