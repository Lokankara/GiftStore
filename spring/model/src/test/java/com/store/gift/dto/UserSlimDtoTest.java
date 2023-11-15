package com.store.gift.dto;

import com.store.gift.entity.RoleType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserSlimDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("Given a UserSlimDto object, when its properties are set, it should return the correct values")
    void testUserSlimDtos() {
        Long id = 1L;
        String username = "user";
        String email = "test@test.com";
        String password = "password";
        RoleType roleType = RoleType.USER;
        RoleDto role = RoleDto.builder().permission(roleType).id(id).build();
        role.setId(id);
        role.setPermission(roleType);
        UserSlimDto user = new UserSlimDto(id, username, email, password, role);
        assertEquals(user.getId(), id);
        assertEquals(user.getUsername(), username);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getRole(), role);
        assertEquals(user.getRole().getPermission(), role.getPermission());
        assertEquals(user.getRole().getId(), role.getId());
    }

    @Test
    @DisplayName("Given a UserSlimDto Builder, when its properties are set, it should return the correct values")
    void testUserSlimDtoBuilder() {
        Long id = 1L;
        String username = "user";
        String email = "test@test.com";
        String password = "password";
        RoleDto role = RoleDto.builder().permission(RoleType.USER).build();
        UserSlimDto user = UserSlimDto.builder().id(id).username(username).email(email).password(password).role(role).build();
        assertEquals(user.getId(), id);
        assertEquals(user.getUsername(), username);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getRole(), role);
    }

    @Test
    void testUserDto() {
        Set<OrderDto> orders = new HashSet<>();
        BigDecimal cost = new BigDecimal("99.99");
        Timestamp date = Timestamp.valueOf("2023-07-29 00:12:38");
        orders.add(OrderDto.builder().orderDate(date).cost(cost).build());
        OrderDto orderDto = new OrderDto();
        orderDto.setId(2L);
        orderDto.setOrderDate(Timestamp.valueOf("2023-07-30 00:12:38"));
        orderDto.setCost(new BigDecimal("199.99"));
        orders.add(orderDto);

        RoleDto role = new RoleDto(1L, RoleType.USER);

        UserDto user = new UserDto(
                1L,
                "user",
                "testuser@example.com",
                orders,
                role);

        UserDto build = UserDto.builder()
                .id(1L)
                .username("user")
                .email("testuser@example.com")
                .orderDtos(orders)
                .role(role)
                .build();

        assertEquals(build, user);
        assertEquals(1L, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals(orders, user.getOrderDtos());
        assertEquals(role, user.getRole());
    }

    @Test
    @DisplayName("Given a UserDto object, when the username is null or its length is not between 1 and 128, then validation should fail")
    void testUsernameConstraints() {
        UserDto user = new UserDto();
        user.setUsername(null);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setUsername("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setUsername("a".repeat(129));
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Given a UserDto object, when the email is null or its length is not between 1 and 128, then validation should fail")
    void testEmailConstraints() {
        UserDto user = new UserDto();
        user.setEmail(null);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setEmail("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setEmail("a".repeat(129));
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
