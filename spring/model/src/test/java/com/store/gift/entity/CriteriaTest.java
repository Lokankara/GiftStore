package com.store.gift.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class CriteriaTest {

    @Test
    @DisplayName("Given name, description, and tagNames, when Criteria is built, then name, description, and tagNames are set correctly")
    void testCriteriaBuilder() {
        String name = "Year";
        String season = "Season";
        List<String> tagNames = List.of("Summer", "Fall");
        Criteria criteria = Criteria.builder()
                .name(name)
                .description(season)
                .tagNames(tagNames)
                .build();
        assertEquals(name, criteria.getName());
        assertEquals(season, criteria.getDescription());
        assertEquals(tagNames, criteria.getTagNames());
        criteria.setName(name);
        criteria.setDescription(season);
        criteria.setTagNames(tagNames);
        assertEquals(name, criteria.getName());
        assertEquals(season, criteria.getDescription());
        assertEquals(tagNames, criteria.getTagNames());
    }
}
