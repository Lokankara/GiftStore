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

/**
 * Data transfer object for representing a slim version of a user.
 * Inherits from the RepresentationModel class for HATEOAS support.
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserSlimDto extends RepresentationModel<UserSlimDto> {
    /**
     * The ID of the user.
     */
    private Long id;
    /**
     * The username of the user.
     * The password must be between 1 and 128 characters in length.
     */
    @Size(min = 1, max = 128)
    @NotNull(message = "Name cannot be blank")
    private String username;

    /**
     * The email of the user.
     * It cannot be blank (null).
     */
    @Size(min = 1, max = 128)
    private String email;
    /**
     * Represents a user's password.
     * The password must be between 1 and 128 characters in length.
     * It cannot be blank (null).
     */
    @Size(min = 1, max = 128)
    @NotNull(message = "Password cannot be blank")
    private String password;
    /**
     * Represents the role assigned to the user.
     */
    private RoleDto role;

    @JsonCreator
    public UserSlimDto(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("role") RoleDto role
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
