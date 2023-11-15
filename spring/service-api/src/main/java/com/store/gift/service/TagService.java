package com.store.gift.service;

import com.store.gift.dto.TagDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing tags.
 * <p>
 * Provides methods to retrieve, save, and delete tags.
 */

public interface TagService {

    /**
     * Get a tag by ID.
     *
     * @param id the tag ID
     * @return the tag DTO
     */
    TagDto getById(Long id);

    /**
     * Get a tag by name.
     *
     * @param name the tag name
     * @return the tag DTO
     */
    TagDto getByName(String name);

    /**
     * Get all tags with pagination.
     *
     * @param pageable the pageable information
     * @return the list of tag DTOs
     */
    List<TagDto> getAll(Pageable pageable);

    /**
     * Save a tag.
     *
     * @param tag the tag DTO to save
     * @return the saved tag DTO
     */
    TagDto save(TagDto tag);

    /**
     * Delete a tag by ID.
     *
     * @param id the tag ID
     */
    void delete(Long id);
}
