CREATE TABLE IF NOT EXISTS invoices
(
    id             BIGSERIAL PRIMARY KEY,
    counter        BIGINT,
    order_id       BIGINT,
    certificate_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    FOREIGN KEY (certificate_id) REFERENCES gift_certificates (id)
);