package com.store.gift.mapper;

import com.store.gift.dto.OrderDto;
import com.store.gift.dto.UserDto;
import com.store.gift.dto.UserSlimDto;
import com.store.gift.entity.Order;
import com.store.gift.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

/**
 * This interface defines mapping methods between User and UserDto objects.
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    /**
     * Converts a UserDto object to a User entity.
     *
     * @param userDto The UserDto object to convert.
     * @return The corresponding User entity.
     */
    @Mapping(target = "orders", source = "orderDtos", qualifiedByName = "toOrderSet")
    @Mapping(target = "role", source = "role", qualifiedByName = "toRole")
    User toEntity(UserDto userDto);
    @Mapping(target = "role", source = "role", qualifiedByName = "toRole")
    User toEntity(UserSlimDto userDto);

    /**
     * Converts a User entity to a UserDto object.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserDto object.
     */
    @Mapping(target = "orderDtos", source = "orders", qualifiedByName = "toOrderDtoSet")
    @Mapping(target = "role", source = "role", qualifiedByName = "toRoleDto")
    UserDto toDto(User user);

    /**
     * Converts a list of User entities to a list of UserDto objects.
     *
     * @param users The list of User entities to convert.
     * @return A list of corresponding UserDto objects.
     */
    @Mapping(target = "orderDtos", source = "orders")
    List<UserDto> toDtoList(List<User> users);

    /**
     * Converts a set of OrderDto objects to a set of Order entities.
     *
     * @param orderDtos The set of OrderDto objects to convert.
     * @return A set of corresponding Order entities.
     */
    @Named("toOrderSet")
    Set<Order> toOrderSet(Set<OrderDto> orderDtos);

    /**
     * Converts a set of Order entities to a set of OrderDto objects.
     *
     * @param orders The set of Order entities to convert.
     * @return A set of corresponding OrderDto objects.
     */
    @Named("toOrderDtoSet")
    Set<OrderDto> toOrderDtoSet(Set<Order> orders);
}
