package com.store.gift.service;

import com.store.gift.dao.TagDao;
import com.store.gift.dto.TagDto;
import com.store.gift.entity.Tag;
import com.store.gift.exception.TagAlreadyExistsException;
import com.store.gift.exception.TagNotFoundException;
import com.store.gift.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service implementation for managing tags.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    /**
     * The Tag DAO (Data Access Object).
     */
    private final TagDao tagDao;

    /**
     * The Tag Mapper for converting between entities and DTOs.
     */
    private final TagMapper tagMapper;

    /**
     * The error message used when a tag is not found.
     */
    private static final String MESSAGE = "Tag not found by";

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag
     * @return the tag DTO
     * @throws TagNotFoundException if the tag is not found
     */
    @Override
    @Transactional(readOnly = true)
    public TagDto getById(final Long id) {
        Objects.requireNonNull(id, "Id should not be null");
        Tag tag = tagDao.getById(id)
                .orElseThrow(() -> new TagNotFoundException(
                        String.format("%s id: %s", MESSAGE, id)));
        return tagMapper.toDto(tag);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a tag by its name.
     *
     * @param name the name of the tag
     * @return the tag DTO
     * @throws TagNotFoundException if the tag is not found
     */
    @Override
    @Transactional(readOnly = true)
    public TagDto getByName(final String name) {
        Objects.requireNonNull(name, "Name should not be null");
        Tag tag = tagDao.findByUsername(name)
                .orElseThrow(() -> new TagNotFoundException(
                        String.format("%s name: %s", MESSAGE, name)));
        return tagMapper.toDto(tag);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all tags with pagination.
     *
     * @param pageable the pagination information
     * @return the list of tag DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<TagDto> getAll(final Pageable pageable) {
        return tagDao.getAllBy(pageable)
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Saves a new tag.
     *
     * @param dto the tag DTO to be saved
     * @return the saved tag DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TagDto save(final TagDto dto) {
        if (tagDao.findByUsername(dto.getName()).isPresent()) {
            throw new TagAlreadyExistsException(String.format(
                    "Tag already exists exception with name %s", dto.getName()));
        }
        Tag saved = tagDao.save(tagMapper.toEntity(dto));
        return tagMapper.toDto(saved);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deletes a tag by its ID.
     *
     * @param id the ID of the tag to be deleted
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "Id should not be null");
        tagDao.delete(id);
    }
}
