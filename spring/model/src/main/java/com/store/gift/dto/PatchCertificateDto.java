package com.store.gift.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PatchCertificateDto
        extends RepresentationModel<CertificateDto> {

    /**
     * The ID of the certificate.
     */
    private Long id;
    /**
     * The price of the certificate.
     */
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0.00")
    @DecimalMax(value = "10000.00", inclusive = false, message = "Price must be less than 10000.00")
    private BigDecimal price;

    /**
     * The duration of the certificate.
     */
    @Min(value = 0, message = "Duration must be a positive number or zero.")
    @Max(value = 720, message = "Duration must be less than or equal to 720.")
    private int duration;

    /**
     * Set of TagDto objects associated with this entity.
     * This field is excluded from the generated toString() and equals() / hashCode() methods.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TagDto> tags;

    /**
     * Handles unknown properties during deserialization.
     * Throws an exception indicating that the field is not allowed for update.
     *
     * @param key   the name of the unknown property
     * @param value the value of the unknown property
     * @throws IllegalArgumentException if an unknown property is encountered
     */
    @JsonAnySetter
    public void handleUnknown(final String key, final Object value) {
        throw new IllegalArgumentException(
                String.format("Field %s is not allowed for update: %s", key, value));
    }

    /**
     * Constructs a new PatchCertificateDto object.
     *
     * @param id       the ID of the certificate
     * @param price    the updated price of the certificate
     * @param duration the updated duration of the certificate
     * @param tags     the updated tags of the certificate
     */
    @JsonCreator
    public PatchCertificateDto(
            @JsonProperty("id") Long id,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("duration") int duration,
            @JsonProperty("tags") Set<TagDto> tags) {
        this.id = id;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
    }
}
