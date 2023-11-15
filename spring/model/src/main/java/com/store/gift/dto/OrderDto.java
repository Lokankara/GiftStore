package com.store.gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

/**
 * This class represents an Order Data Transfer Object (DTO)
 * that extends the {@link org.springframework.hateoas.RepresentationModel} class.
 * <p>
 * It is annotated with {@link lombok.Data}, {@link lombok.Builder}
 * and {@link lombok.EqualsAndHashCode} with callSuper set to false.
 * <p>
 * The {@link lombok.Builder} annotation produces complex builder APIs for the class.
 * <p>
 * The {@link lombok.EqualsAndHashCode} annotation generates implementations
 * of the `equals` and `hashCode` methods. By setting callSuper to false,
 * the generated methods will not include a call
 * to the superclass implementations of these methods.
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDto extends RepresentationModel<OrderDto> {
    /**
     * The ID of the order.
     */
    private Long id;

    /**
     * The user associated with the order.
     */
    @NotNull(message = "User cannot be blank")
    private UserSlimDto user;

    /**
     * The cost of the order.
     */
    @DecimalMax(value = "100000.00", inclusive = false, message = "Price must be less than 100000.00")
    private BigDecimal cost;

    /**
     * The date of the order.
     */
    @JsonFormat(timezone = "GMT+03:00", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp orderDate;

    /**
     * The set of certificates included in the order.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CertificateDto> certificateDtos;

    @JsonCreator
    public OrderDto(
            @JsonProperty("id") Long id,
            @JsonProperty("user") UserSlimDto user,
            @JsonProperty("cost") BigDecimal cost,
            @JsonProperty("orderDate") Timestamp orderDate,
            @JsonProperty("certificateDtos") Set<CertificateDto> certificateDtos) {
        this.id = id;
        this.cost = cost;
        this.user = user;
        this.orderDate = orderDate;
        this.certificateDtos = certificateDtos;
    }
}
