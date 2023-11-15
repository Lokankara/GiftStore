package com.store.gift.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class OrderSlimDto extends RepresentationModel<OrderDto> {
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
    @DecimalMax(value = "100000.00", inclusive = false,
            message = "Price must be less than 100000.00")
    private BigDecimal cost;

    /**
     * The date of the order.
     */
    @JsonFormat(timezone = "GMT+03:00",
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp orderDate;
}
