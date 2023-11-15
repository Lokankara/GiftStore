package com.store.gift.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AuditListener {

    /**
     * Pre-persist hook to set the entity date.
     * Callback method invoked before the entity is persisted.
     * It sets the create and last update dates to the current timestamp.
     *
     * @param entity the entity being created
     */
    @PrePersist
    public void prePersist(final Object entity) {
        if (entity instanceof Order order) {
            order.setOrderDate(now());
        }

        if (entity instanceof Certificate certificate) {
            certificate.setCreateDate(now());
            certificate.setLastUpdateDate(now());
        }
    }

    /**
     * Callback method invoked before the entity is updated.
     * It updates the last update date to the current timestamp
     *
     * @param entity the entity being updated
     */
    @PreUpdate
    public void preUpdate(final Object entity) {
        if (entity instanceof Certificate certificate) {
            certificate.setLastUpdateDate(now());
        }
    }

    private static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
