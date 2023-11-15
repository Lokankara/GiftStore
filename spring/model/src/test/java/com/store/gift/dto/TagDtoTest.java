package com.store.gift.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TagDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testNoArgsConstructor() {
        TagDto tagDto = new TagDto(0L, "");
        assertNotNull(tagDto);
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Test Tag";
        TagDto tagDto = TagDto.builder().id(id).name(name).build();
        assertEquals(id, tagDto.getId());
        assertEquals(name, tagDto.getName());
    }

    @Test
    void testGetterAndSetter() {
        Long id = 1L;
        String name = "Test Tag";
        TagDto tagDto = new TagDto(id, name);
        tagDto.setId(id);
        tagDto.setName(name);

        assertEquals(id, tagDto.getId());
        assertEquals(name, tagDto.getName());
    }

    @Test
    void testValidation() {
        TagDto tagDto = new TagDto(1L, "");
        var violations = validator.validate(tagDto);
        assertEquals(1, violations.size());
    }

    @Test
    void testJsonCreator() {
        Long id = 1L;
        String name = "Test Tag";
        TagDto tagDto = new TagDto(id, name);
        assertEquals(id, tagDto.getId());
        assertEquals(name, tagDto.getName());
    }
}
