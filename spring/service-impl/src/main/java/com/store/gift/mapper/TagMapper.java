package com.store.gift.mapper;

import com.store.gift.dto.TagDto;
import com.store.gift.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Mapper interface for mapping between {@link Tag} and {@link TagDto}.
 */
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TagMapper {

    /**
     * Maps a {@link Tag} entity to a {@link TagDto}.
     *
     * @param tag the {@link Tag} entity.
     * @return the mapped {@link TagDto}.
     */
    TagDto toDto(Tag tag);

    /**
     * Maps a {@link TagDto} to a {@link Tag} entity.
     *
     * @param dto the {@link TagDto}.
     * @return the mapped {@link Tag} entity.
     */
    Tag toEntity(TagDto dto);

    /**
     * Maps a set of {@link TagDto} to a set of {@link Tag} entities.
     *
     * @param tagDtos the set of {@link TagDto}.
     * @return the mapped set of {@link Tag} entities.
     */
    @Named("toTagSet")
    default Set<Tag> toTagSet(Set<TagDto> tagDtos) {
        return tagDtos == null ? new HashSet<>()
                : tagDtos.stream()
                .map(this::toEntity)
                .collect(toSet());
    }

    /**
     * Maps a set of {@link Tag} entities to a set of {@link TagDto}.
     *
     * @param tags the set of {@link Tag} entities.
     * @return the mapped set of {@link TagDto}.
     */
    @Named("toTagDtoSet")
    default Set<TagDto> toDtoSet(Set<Tag> tags) {
        return tags == null ? new HashSet<>()
                : tags.stream()
                .map(this::toDto)
                .collect(toSet());
    }
}
