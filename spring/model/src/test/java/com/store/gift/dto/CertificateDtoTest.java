package com.store.gift.dto;

import com.store.gift.entity.Certificate;
import com.store.gift.entity.Order;
import com.store.gift.entity.Tag;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateDtoTest {
    private Certificate certificate2;
    private Tag tag;
    private Order order;
    private Certificate certificate;
    private CertificateDto dto;
    private final Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
    private final Timestamp lastUpdateDate = Timestamp.valueOf(LocalDateTime.now());

    @BeforeEach
    public void setup() {
        dto = new CertificateDto();
        order = Order.builder().build();
        certificate = Certificate.builder().lastUpdateDate(lastUpdateDate).createDate(createDate).build();
        certificate2 = Certificate.builder().lastUpdateDate(lastUpdateDate).createDate(createDate).build();
        tag = Tag.builder().build();
    }

    @Test
    @DisplayName("Add existing Certificate to Tag")
    void testAddExistingCertificate() {
        tag.addCertificate(certificate);
        assertTrue(tag.getCertificates().contains(certificate));
        assertTrue(certificate.getTags().contains(tag));
        assertEquals(lastUpdateDate, certificate.getLastUpdateDate());
    }

    @Test
    @DisplayName("Add non-existing Certificate to Tag")
    void testAddNonExistingCertificate() {
        tag.addCertificate(null);
        assertTrue(tag.getCertificates().isEmpty());
    }

    @Test
    @DisplayName("Remove existing Certificate from Tag")
    void testRemoveExistingCertificate() {
        tag.addCertificate(certificate);
        tag.removeCertificate(certificate);
        assertFalse(tag.getCertificates().contains(certificate));
        assertFalse(certificate.getTags().contains(tag));
    }

    @Test
    @DisplayName("Remove non-existing Certificate from Tag")
    void testRemoveNonExistingCertificate() {
        Tag removeCertificate = tag.removeCertificate(certificate);
        assertFalse(certificate.getTags().contains(removeCertificate));
    }

    @Test
    @DisplayName("Remove certificate from order")
    void testAddAndRemoveCertificate() {
        order.addCertificate(certificate);
        assertTrue(order.getCertificates().contains(certificate));
        assertTrue(certificate.getOrders().contains(order));
        order.addCertificate(certificate2);
        assertTrue(order.getCertificates().contains(certificate2));
        assertTrue(certificate2.getOrders().contains(order));
        order.removeCertificate(certificate);
        assertFalse(order.getCertificates().contains(certificate));
        assertFalse(certificate.getOrders().contains(order));
    }


    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testCertificateDto(
            Long id, String name, String description,
            BigDecimal price, int duration) {
        dto.setId(id);
        dto.setName(name);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setDuration(duration);
        dto.setCreateDate(createDate);
        dto.setLastUpdateDate(lastUpdateDate);
        dto.setTags(new HashSet<>());
        dto.setOrderDtos(new HashSet<>());
        CertificateDto certificateDto = dto;
        assertEquals(certificateDto, dto);
        assertEquals(certificateDto.hashCode(), dto.hashCode());
        assertTrue(dto.getOrderDtos().isEmpty());
        assertTrue(dto.getTags().isEmpty());
        assertEquals(lastUpdateDate, dto.getLastUpdateDate());
        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(price, dto.getPrice());
        assertEquals(description, dto.getDescription());
        assertEquals(duration, dto.getDuration());
        assertEquals(createDate, dto.getCreateDate());
        assertEquals(lastUpdateDate, dto.getLastUpdateDate());
    }
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testRemoveCertificateFromOrder(
            Long id, String name, String description,
            BigDecimal price, int duration) {
        Certificate certificate = Certificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();

        CertificateDto certificateDto = dto;
        assertEquals(certificateDto, dto);
        assertEquals(certificateDto.hashCode(), dto.hashCode());
        assertNotEquals(certificate.hashCode(), certificate2.hashCode());
        order.addCertificate(certificate);
        order.addCertificate(certificate2);
        order.removeCertificate(certificate);
        int initialSize = order.getCertificates().size();
        assertEquals(createDate, certificate.getCreateDate());
        assertEquals(lastUpdateDate, certificate.getLastUpdateDate());
        assertFalse(order.getCertificates().contains(certificate));
        assertFalse(certificate.getOrders().contains(order));
        assertTrue(order.getCertificates().contains(certificate2));
        assertTrue(certificate2.getOrders().contains(order));
        assertEquals(initialSize, order.getCertificates().size());
        assertFalse(order.getCertificates().contains(certificate));
        assertFalse(certificate.getOrders().contains(order));
        assertTrue(certificate2.getOrders().contains(order));
        assertEquals(initialSize, order.getCertificates().size());
        assertNotNull(order.removeCertificate(certificate2));
        assertEquals(initialSize - 1, order.getCertificates().size());
        assertFalse(order.getCertificates().contains(certificate2));
        assertFalse(certificate2.getOrders().contains(order));
        assertFalse(order.getCertificates().contains(certificate2));
        assertFalse(certificate2.getOrders().contains(order));
    }

    @DisplayName("Test remove non existing certificate")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testRemoveNonExistingCertificate(
            long id, String name, String description,
            BigDecimal price, int duration) {
        Certificate certificate = Certificate.builder()
                .id(id).name(name)
                .price(price)
                .lastUpdateDate(lastUpdateDate)
                .description(description)
                .build();
        certificate.setId(id);
        certificate.setName(name);
        certificate.setPrice(price);
        certificate.setDuration(duration);
        order.addCertificate(certificate);
        certificate2.setName(name);
        int initialSize = order.getCertificates().size();
        order.removeCertificate(certificate2);
        assertNotNull(certificate.getLastUpdateDate());
        assertNotNull(certificate.getName());
        assertNotNull(certificate.getId());
        assertNotNull(certificate.getPrice());
        assertNotNull(certificate.getDescription());
        assertNotNull(certificate.getLastUpdateDate());
        assertNotNull(certificate.getLastUpdateDate());
        assertNotNull(certificate.getLastUpdateDate());
        assertEquals(initialSize, order.getCertificates().size());
        assertTrue(order.getCertificates().contains(certificate));
        assertTrue(certificate.getOrders().contains(order));
        assertFalse(order.getCertificates().contains(certificate2));
        assertFalse(certificate2.getOrders().contains(order));
    }

    @Test
    @DisplayName("Add certificate to order")
    void testAddAndCertificate() {
        order.addCertificate(certificate);
        assertTrue(order.getCertificates().contains(certificate));
        assertTrue(certificate.getOrders().contains(order));
    }

    @Test
    @DisplayName("Remove Certificate from Tag")
    void testAddCertificateAndRemoveCertificate() {
        tag.addCertificate(new Certificate());
        tag.removeCertificate(certificate);
        assertFalse(tag.getCertificates().contains(certificate));
        assertFalse(certificate.getTags().contains(tag));
    }

    @Test
    @DisplayName("Remove Certificate from Order")
    void testRemoveOrder() {
        certificate.addOrder(order);
        certificate.removeOrder(order);
        assertFalse(certificate.getOrders().contains(order));
        assertFalse(order.getCertificates().contains(certificate));
    }

    @Test
    @DisplayName("Remove Tags from Certificate")
    void testRemoveTags() {
        certificate.addTag(new Tag());
        certificate.removeTag(Tag.builder().build());
        assertFalse(certificate.getOrders().contains(order));
        assertFalse(order.getCertificates().contains(certificate));
    }

    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void certificateDtoValidation(Long id, String name, String description, BigDecimal price, int duration) {
        CertificateDto certificateDto = getCertificateDto(id, name, description, price, duration);
        certificateDto.setCreateDate(createDate);
        assertSame(certificateDto.getCreateDate(), createDate);
        assertDoesNotThrow(() -> validateCertificateDto(certificateDto));
    }

    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75",
            "5, , , 20.0, 30",
            "6, Summer, , , 40",
            "7, , Season 3, , 50",
            "8, Autumn, Season 4, -10.0, 60",
            "9, Winter, Season 1, 10.0, -5"})
    void certificateDtoValidationsNeg(Long id, String name, String description, BigDecimal price, int duration) {
        CertificateDto certificateDto = getCertificateDto(id, name, description, price, duration);

        if (id != null && price != null && name != null && duration > 0) {
            assertDoesNotThrow(() -> validateCertificateDto(certificateDto));
        } else {
            assertThrows(ConstraintViolationException.class, () -> validateCertificateDto(certificateDto));
        }
    }

    private void validateCertificateDto(CertificateDto certificateDto) {

        if (certificateDto.getId() == null) {
            throw new ConstraintViolationException("Id cannot be null", null);
        }
        if (certificateDto.getName() == null || certificateDto.getName().isEmpty()) {
            throw new ConstraintViolationException("Name cannot be blank", null);
        }
        if (certificateDto.getDescription() == null || certificateDto.getDescription().isEmpty()) {
            throw new ConstraintViolationException("Description cannot be blank", null);
        }
        if (certificateDto.getPrice() == null) {
            throw new ConstraintViolationException("Price cannot be null", null);
        }
        if (certificateDto.getDuration() <= 0) {
            throw new ConstraintViolationException("Duration must be a positive value", null);
        }
    }

    private static CertificateDto getCertificateDto(
            Long id, String name, String description, BigDecimal price, int duration) {
        return CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .tags(new HashSet<>())
                .orderDtos(new HashSet<>())
                .duration(duration)
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }
}
