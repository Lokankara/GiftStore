package com.store.gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object (DTO) class representing a tag.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class TagDto extends RepresentationModel<TagDto> {
    /**
     * The ID of the tag.
     */
    private Long id;

    /**
     * The name of the tag.
     * Size: minimum 1 character, maximum 512 characters.
     * Not null: Name cannot be blank.
     */
    @Size(min = 1, max = 512)
    @NotNull(message = "Name cannot be blank")
    private String name;

    @JsonCreator
    public TagDto(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
