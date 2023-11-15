package com.store.gift.dto;

import com.store.gift.entity.Order;
import com.store.gift.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDtoTest {

    @Test
    void testOrderDto() {
        UserSlimDto user = new UserSlimDto();
        Set<CertificateDto> certificateDtos = new HashSet<>();
        Timestamp date = new Timestamp(System.currentTimeMillis());
        long id = 1L;
        BigDecimal decimal = new BigDecimal("100.00");
        OrderDto orderDto = OrderDto.builder()
                .id(id)
                .user(user)
                .cost(decimal)
                .orderDate(date)
                .certificateDtos(certificateDtos)
                .build();

        assertEquals(id, orderDto.getId());
        assertEquals(user, orderDto.getUser());
        assertEquals(decimal, orderDto.getCost());
        assertEquals(certificateDtos, orderDto.getCertificateDtos());
        assertEquals(id, orderDto.getId());
        assertEquals(date, orderDto.getOrderDate());
    }

    @Test
    void testOrder() {
        User user = new User();
        Timestamp date = new Timestamp(System.currentTimeMillis());
        long id = 1L;
        BigDecimal decimal = new BigDecimal("100.00");
        Order orderDto = Order.builder()
                .id(id)
                .user(user)
                .cost(decimal)
                .orderDate(date)
                .build();

        assertEquals(id, orderDto.getId());
        assertEquals(user, orderDto.getUser());
        assertEquals(decimal, orderDto.getCost());
        assertEquals(id, orderDto.getId());
        assertEquals(date, orderDto.getOrderDate());
    }
}
