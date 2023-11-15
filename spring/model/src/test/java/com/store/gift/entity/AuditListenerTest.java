package com.store.gift.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuditListenerTest {
    private Order order;
    private Certificate certificate;
    private AuditListener auditListener;
    private static final long DELTA = 1000;

    @BeforeEach
    public void setup() {
        auditListener = new AuditListener();
        order = Order.builder().build();
        certificate = Certificate.builder().build();
    }

    @Test
    void testPrePersistCertificate() {
        Certificate certificate = new Certificate();
        auditListener.prePersist(certificate);
        assertNotNull(certificate.getCreateDate());
        assertNotNull(certificate.getLastUpdateDate());
    }

    @Test
    @DisplayName("Given new Order and Certificate, when prePersist is called, then orderDate, createDate, and lastUpdateDate are set to now")
    void testPrePersist() {
        auditListener.prePersist(order);
        auditListener.prePersist(certificate);

        long orderDateDiff = Math.abs(order.getOrderDate().getTime()
                - Timestamp.valueOf(LocalDateTime.now()).getTime());
        long createDateDiff = Math.abs(certificate.getCreateDate().getTime()
                - Timestamp.valueOf(LocalDateTime.now()).getTime());
        long lastUpdateDateDiff = Math.abs(certificate.getLastUpdateDate().getTime()
                - Timestamp.valueOf(LocalDateTime.now()).getTime());

        assertTrue(orderDateDiff < DELTA);
        assertTrue(createDateDiff < DELTA);
        assertTrue(lastUpdateDateDiff < DELTA);
    }

    @Test
    @DisplayName("Given new Certificate, when preUpdate is called, then lastUpdateDate is set to now")
    void testPreUpdate() {
        auditListener.preUpdate(certificate);
        long lastUpdateDateDiff = Math.abs(certificate.getLastUpdateDate().getTime()
                - Timestamp.valueOf(LocalDateTime.now()).getTime());
        assertTrue(lastUpdateDateDiff < DELTA);
    }
}
