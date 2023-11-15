package com.store.gift.mapper;

import com.store.gift.dto.OrderDto;
import com.store.gift.dto.OrderSlimDto;
import com.store.gift.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for converting between Order and OrderDto objects.
 */
@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CertificateMapper.class})
public interface OrderMapper {

    /**
     * Converts an {@link Order} entity to an {@link OrderDto} DTO.
     *
     * @param order the {@link Order} entity to convert
     * @return the corresponding {@link OrderDto} DTO
     */
    @Mapping(source = "certificates", target = "certificateDtos")
    @Mapping(target = "user", ignore = true)
    OrderDto toDto(Order order);

    /**
     * Converts an {@link OrderDto} DTO to an {@link Order} entity.
     *
     * @param orderDto the {@link OrderDto} DTO to convert
     * @return the corresponding {@link Order} entity
     */
    @Mapping(source = "user", target = "user")
    Order toEntity(OrderDto orderDto);

    /**
     * Converts a list of {@link Order} entities to a list of {@link OrderDto} DTOs.
     *
     * @param orders the list of {@link Order} entities to convert
     * @return the corresponding list of {@link OrderDto} DTOs
     */
    @Mapping(target = "certificateDtos", source = "certificates")
    @Mapping(source = "user", target = "user")
    List<OrderDto> toDtoList(List<Order> orders);

    @Mapping(target = "certificateDtos", source = "certificates")
    @Mapping(source = "user", target = "user")
    List<OrderSlimDto> toSlimDtoList(List<Order> orders);

    /**
     * Converts a list of {@link OrderDto} DTOs to a list of {@link Order} entities.
     *
     * @param orderDtos the list of {@link OrderDto} DTOs to convert
     * @return the corresponding list of {@link Order} entities
     */
    @Mapping(target = "certificates", source = "certificateDtos")
    List<Order> toEntityList(List<OrderDto> orderDtos);
}
