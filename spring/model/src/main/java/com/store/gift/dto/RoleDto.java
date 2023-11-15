package com.store.gift.dto;

import com.store.gift.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

/**
 * The RoleDto class represents a DTO (Data Transfer Object) for the Role entity.
 * It contains information about the role's ID and permission.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class RoleDto extends RepresentationModel<RoleDto> {
    /**
     * The ID of the role.
     */
    private Long id;
    /**
     * The permission associated with the role.
     */
    @Enumerated(EnumType.STRING)
    private RoleType permission;

    /**
     * Constructs a new RoleDto object with the specified ID and permission.
     *
     * @param id         the ID of the role
     * @param permission the permission associated with the role
     */
    @JsonCreator
    public RoleDto(
            @JsonProperty("id") Long id,
            @JsonProperty("username") RoleType permission) {
        this.id = id;
        this.permission = permission;
    }
}
