package com.store.gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for User.
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserDto extends RepresentationModel<UserDto> {
    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    @Size(min = 1, max = 128)
    @NotNull(message = "Name cannot be blank")
    private String username;

    /**
     * The email of the user.
     */
    @Size(min = 1, max = 128)
    @NotNull(message = "Email cannot be blank")
    private String email;

    /**
     * The set of order DTOs associated with the user.
     */
    private Set<OrderDto> orderDtos;
    /**
     * Represents the role assigned to the user.
     */
    private RoleDto role;

    @JsonCreator
    public UserDto(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("orderDtos") Set<OrderDto> orderDtos,
            @JsonProperty("role") RoleDto role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.orderDtos = orderDtos;
        this.role = role;
    }
}
