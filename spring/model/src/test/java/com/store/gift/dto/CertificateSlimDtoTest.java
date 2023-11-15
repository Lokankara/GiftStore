package com.store.gift.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import static java.sql.Timestamp.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateSlimDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    Long id;
    String name;
    String description;
    BigDecimal price;
    int duration;
    Timestamp createDate;
    Timestamp lastUpdateDate;
    CertificateSlimDto certificate;

    @BeforeEach
    public void setup() {
        id = 1L;
        name = "Name";
        description = "Description";
        price = BigDecimal.valueOf(9999.99);
        duration = 720;
        createDate = valueOf(LocalDateTime.now());
        lastUpdateDate = valueOf(LocalDateTime.now());
        certificate = CertificateSlimDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price)
                .build();
    }

    @Test
    @DisplayName("Test CertificateSlimDto validation")
    void testCertificateSlimDtoValidation() {
        CertificateSlimDto certificate = new CertificateSlimDto(id, name, description, price, duration, createDate, lastUpdateDate, "http");
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(certificate);
        assertTrue(violations.isEmpty());
        certificate.setName("");
        violations = validator.validate(certificate);
        assertEquals(1, violations.size());

        validator.validate(certificate).forEach(violation -> assertEquals(
                "size must be between 1 and 512", violation.getMessage()));

        certificate.setName("Name");
        certificate.setPrice(BigDecimal.valueOf(10000));
        violations = validator.validate(certificate);
        assertEquals(1, violations.size());

        validator.validate(certificate).forEach(violation -> assertEquals(
                "Price must be less than 10000.00", violation.getMessage()));

        certificate.setPrice(BigDecimal.valueOf(9999.99));
        certificate.setDuration(766);
        violations = validator.validate(certificate);
        assertEquals(1, violations.size());
        validator.validate(certificate).forEach(violation -> assertEquals(
                "Duration must be less than or equal to 720.", violation.getMessage()));
    }

    @Test
    @DisplayName("Given a valid name, when CertificateSlimDto is validated, then no validation errors are generated")
    void testValidName() {
        CertificateSlimDto slimDto =
                getDto(id, name, description, price, duration, createDate, lastUpdateDate);
        Set<ConstraintViolation<CertificateSlimDto>> violations =
                validator.validate(slimDto);

        slimDto.setId(id);
        slimDto.setName(name);
        slimDto.setPrice(price);
        slimDto.setDuration(duration);
        slimDto.setCreateDate(createDate);
        slimDto.setDescription(description);
        slimDto.setLastUpdateDate(lastUpdateDate);

        assertTrue(violations.isEmpty());
        assertEquals(slimDto.getId(), id);
        assertEquals(slimDto.getName(), name);
        assertEquals(slimDto.getDescription(), description);
        assertEquals(slimDto.getPrice(), price);
        assertEquals(slimDto.getDuration(), duration);
        assertEquals(slimDto.getCreateDate(), createDate);
        assertEquals(slimDto.getLastUpdateDate(), lastUpdateDate);
    }

    @Test
    @DisplayName("Given an invalid name, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidName() {
        CertificateSlimDto certificate = getDto(id, "", description, price, duration, createDate, lastUpdateDate);
        validator.validate(certificate).forEach(violation -> assertEquals(
                "size must be between 1 and 512", violation.getMessage()));
    }

    @Test
    @DisplayName("Given an invalid name with length 513, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidNameMax() {
        String invalidName = "a".repeat(513);
        CertificateSlimDto certificate = getDto(id, invalidName, description, price, duration, createDate, lastUpdateDate);
        validator.validate(certificate).forEach(violation -> assertEquals(
                "size must be between 1 and 512", violation.getMessage()));
    }

    @Test
    @DisplayName("Given a valid description, when CertificateSlimDto is validated, then no validation errors are generated")
    void testValidDescription() {
        CertificateSlimDto certificate = getDto(id, name, description, price, duration, createDate, lastUpdateDate);
        assertTrue(validator.validate(certificate).isEmpty());
    }

    @Test
    @DisplayName("Given an invalid description, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidDescription() {
        String invalidDescription = "a".repeat(1025);
        CertificateSlimDto certificate = getDto(id, name, invalidDescription, price, duration, createDate, lastUpdateDate);
        validator.validate(certificate).forEach(violation -> assertEquals(
                "size must be between 1 and 1024", violation.getMessage()));
    }

    @Test
    @DisplayName("Given an invalid price, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidPrice() {
        BigDecimal price = BigDecimal.valueOf(10000.0);
        CertificateSlimDto certificate = getDto(id, name, description, price, duration, createDate, lastUpdateDate);
        validator.validate(certificate).forEach(violation -> assertEquals(
                "Price must be less than 10000.00", violation.getMessage()));
    }

    @Test
    @DisplayName("Given an invalid duration, when CertificateSlimDto is validated, then a validation error is generated with the correct message")
    void testInvalidDuration() {
        CertificateSlimDto certificate =
                getDto(id, name, description, price, 721, createDate, lastUpdateDate);
        validator.validate(certificate).forEach(violation -> assertEquals(
                "Duration must be less than or equal to 720.", violation.getMessage()));
    }

    @Test
    void testFields() {
        CertificateSlimDto certificate = CertificateSlimDto.builder().build();
        certificate.setId(1L);
        certificate.setName("Test Certificate");
        certificate.setDescription("Description");
        certificate.setPrice(new BigDecimal("99.99"));
        certificate.setDuration(30);

        assertNotNull(certificate.getId());
        assertEquals("Test Certificate", certificate.getName());
        assertEquals("Description", certificate.getDescription());
        assertEquals(new BigDecimal("99.99"), certificate.getPrice());
        assertEquals(30, certificate.getDuration());
        assertNull(certificate.getCreateDate());
        assertNull(certificate.getLastUpdateDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CertificateSlimDto certificate1 = CertificateSlimDto.builder().build();
        certificate1.setId(1L);

        CertificateSlimDto certificate2 = CertificateSlimDto.builder().build();
        certificate2.setId(1L);

        CertificateSlimDto certificate3 = CertificateSlimDto.builder().build();
        certificate3.setId(2L);

        assertEquals(certificate1, certificate2);
        assertNotEquals(certificate1, certificate3);
    }

    @Test
    void testToString() {
        certificate.setId(1L);
        certificate.setName("Test Certificate");
        assertNotNull(certificate.toString());
    }

    @Test
    void testRepresentationModel() {
        assertNotNull(certificate.getLinks());
    }

    private static CertificateSlimDto getDto(
            long id, String name, String description,
            BigDecimal price, int duration, Timestamp createDate,
            Timestamp lastUpdateDate) {
        return CertificateSlimDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
    }
}
