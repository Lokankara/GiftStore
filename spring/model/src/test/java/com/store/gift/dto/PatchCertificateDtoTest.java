package com.store.gift.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

class PatchCertificateDtoTest {
    @Test
    void testPatchCertificateDto() {
        Set<TagDto> tags = new HashSet<>();
        long id = 1L;
        int duration = 30;
        tags.add(new TagDto(id, "tag1"));
        tags.add(new TagDto(2L, "tag2"));
        BigDecimal price = new BigDecimal("99.99");

        PatchCertificateDto dto = new PatchCertificateDto(
                id, price, duration, tags);

        dto.setId(id);
        dto.setTags(tags);
        dto.setPrice(price);
        dto.setDuration(duration);
        assertEquals(id, dto.getId());
        assertEquals(price, dto.getPrice());
        assertEquals(duration, dto.getDuration());
        assertEquals(tags, dto.getTags());
    }

    @Test
    void testHandleUnknown() {
        PatchCertificateDto certificate = PatchCertificateDto.builder().build();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            certificate.handleUnknown("unknownField", "unknownValue");});
        String expectedMessage = "Field unknownField is not allowed for update: unknownValue";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
