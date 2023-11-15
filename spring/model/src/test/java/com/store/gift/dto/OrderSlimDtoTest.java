package com.store.gift.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderSlimDtoTest {
    @Test
    void testOrderSlimDto() {
        long id = 1L;
        String username = "user";
        BigDecimal price = new BigDecimal("99.99");
        Timestamp timestamp = Timestamp.valueOf("2023-07-29 00:12:38");
        UserSlimDto user = UserSlimDto.builder()
                .id(id)
                .username(username)
                .build();
        user.setUsername(username);
        OrderSlimDto dto = getDto(id, price, timestamp, user);
        dto.setId(id);
        dto.setCost(price);
        dto.setOrderDate(timestamp);
        assertEquals(dto.hashCode(), dto.hashCode());
        OrderSlimDto slimDto = getDto(id, price, timestamp, user);
        OrderSlimDto otherDto = getDto(id, price, timestamp, user);
        assertEquals(slimDto.hashCode(), otherDto.hashCode());
        assertEquals(slimDto, otherDto);
        assertEquals(id, dto.getId());
        assertEquals(user, dto.getUser());
        assertEquals(price, dto.getCost());
        assertEquals(timestamp, dto.getOrderDate());
    }

    private static OrderSlimDto getDto(
            long id, BigDecimal price, Timestamp timestamp, UserSlimDto user) {
        return OrderSlimDto.builder()
                .id(id)
                .user(user)
                .cost(price)
                .orderDate(timestamp)
                .build();
    }
}
