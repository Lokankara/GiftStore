ALTER SEQUENCE gift_certificate_tag_seq RESTART WITH 21000;
ALTER SEQUENCE order_id_seq RESTART WITH 2100;
ALTER SEQUENCE tag_id_seq RESTART WITH 2100;
ALTER SEQUENCE user_id_seq RESTART WITH 2100;

-- DROP TABLE IF EXISTS gift_certificates;
CREATE TABLE IF NOT EXISTS gift_certificates
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(155),
    description      TEXT,
    price            DECIMAL(10, 2),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP,
    duration         INTEGER
);

CREATE TABLE IF NOT EXISTS tag
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id   SERIAL PRIMARY KEY,
    order_date TIMESTAMP,
    cost       DECIMAL(10, 2)
);

INSERT INTO users (user_id, email, role_id, password, username)
VALUES (1, 'Quartez@i.ua', 4, '$2a$10$6qjF7NTyowumOS9REwyh8O.pbwSIprYP8gvgmxu8lm7SsnPYd9OvG', 'alice'),
       (2, 'Johana@i.ua', 3, '$2a$10$F2C2v6OY8nfqvO01BifRmO4R5pGtnQkFVlPl8F4BseANxOj5yIYfa', 'bob'),
       (3, 'Quenesha@i.ua', 3, '$2a$10$Yg/XbD3nJE7OxJVuelkcr.xVVlhCSH9xkxSDEliZ4P9Ya00beKqnm', 'user');

--
-- CREATE SEQUENCE gift_certificates_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
--
-- ALTER TABLE gift_certificates ALTER COLUMN id SET DEFAULT nextval('gift_certificates_id_seq');
