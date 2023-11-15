package com.store.gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object (DTO) class representing a certificate.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CertificateDto
        extends RepresentationModel<CertificateDto> {

    /**
     * The ID of the certificate.
     */
    private Long id;

    /**
     * The name of the certificate.
     * Size: minimum 1, maximum 512 characters.
     * Cannot be null or blank.
     */
    @Size(min = 1, max = 512)
    @NotNull(message = "Name cannot be blank")
    private String name;

    /**
     * The description of the certificate.
     * Size: minimum 1, maximum 1024 characters.
     */
    @Size(min = 1, max = 1024)
    private String description;
    @Size(min = 1, max = 512)
    private String shortDescription;
    private String company;

    /**
     * The price of the certificate.
     * Minimum value: 0.01 (exclusive).
     * Maximum value: 10000.00 (exclusive).
     */
    @DecimalMin(value = "0.00", inclusive = false,
            message = "Price must be greater than 0.00")
    @DecimalMax(value = "10000.00", inclusive = false,
            message = "Price must be less than 10000.00")
    private BigDecimal price;

    /**
     * The duration of the certificate in days.
     * Minimum value: 0.
     * Maximum value: 720.
     */
    @Min(value = 0, message = "Duration must be a positive number or zero.")
    @Max(value = 720, message = "Duration must be less than or equal to 720.")
    private int duration;

    /**
     * The timestamp when the certificate was created.
     * Format: (e.g., 2023-05-24T10:30:00.000)
     * Timezone: GMT+03:00
     */
    @JsonFormat(timezone = "GMT+03:00",
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp createDate;

    /**
     * The timestamp when the certificate was last updated.
     * Format: (e.g., 2023-05-24T10:30:00.000)
     * Timezone: GMT+03:00
     */
    @JsonFormat(timezone = "GMT+03:00",
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp lastUpdateDate;

    private String path;

    /**
     * The set of tags associated with the certificate.
     * Excluded from the toString() and equals() methods.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TagDto> tags = new HashSet<>();

    /**
     * The set of orders associated with the certificate.
     * Excluded from the toString() and equals() methods.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderDto> orderDtos = new HashSet<>();

    @JsonCreator
    public CertificateDto(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("shortDescription") String shortDescription,
            @JsonProperty("company") String company,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("duration") Integer duration,
            @JsonProperty("createDate") Timestamp createDate,
            @JsonProperty("lastUpdateDate") Timestamp lastUpdateDate,
            @JsonProperty("path") String path,
            @JsonProperty("tags") Set<TagDto> tags,
            @JsonProperty("orderDtos") Set<OrderDto> orderDtos) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.company = "Graviton";
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.path = path;
        this.tags = tags;
        this.orderDtos = orderDtos;
    }
}
