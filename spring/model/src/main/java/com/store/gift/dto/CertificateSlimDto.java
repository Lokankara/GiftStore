package com.store.gift.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a slim version of the Certificate entity.
 * It includes a subset of properties from the full Certificate entity.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
public class CertificateSlimDto
        extends RepresentationModel<CertificateSlimDto> {
    /**
     * The unique identifier of the certificate.
     */
    private Long id;

    /**
     * The name of the certificate.
     * It should be between 1 and 512 characters in length.
     */
    @Size(min = 1, max = 512)
    @NotNull(message = "Name cannot be blank")
    private String name;

    /**
     * The description of the certificate.
     * It should be between 1 and 1024 characters in length.
     */
    @Size(min = 1, max = 1024)
    @NotNull(message = "description cannot be blank")
    private String description;

    /**
     * The price of the certificate.
     * It must be less than 10000.00.
     */
    @NotNull(message = "price cannot be blank")
    @DecimalMax(value = "10000.00", inclusive = false,
            message = "Price must be less than 10000.00")
    private BigDecimal price;

    /**
     * The duration of the certificate in days.
     * It must be less than or equal to 720.
     */
    @NotNull(message = "duration cannot be blank")
    @Max(value = 720, message = "Duration must be less than or equal to 720.")
    private Integer duration;

    /**
     * The creation date of the certificate.
     */
    @JsonFormat(timezone = "GMT+03:00",
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp createDate;

    /**
     * The last update date of the certificate.
     */
    @JsonFormat(timezone = "GMT+03:00",
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp lastUpdateDate;

    private String path;
}
