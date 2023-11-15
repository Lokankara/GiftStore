package com.store.gift.mapper;

import com.store.gift.dto.CertificateDto;
import com.store.gift.dto.TagDto;
import com.store.gift.entity.Certificate;
import com.store.gift.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TestCertificateMapper {

    @InjectMocks
    private TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
    @InjectMocks
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @InjectMocks
    private final CertificateMapper mapper = new CertificateMapperImpl(tagMapper);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("toEntity should map DTO to entity")
    @ParameterizedTest(name = "Map {1} to entity")
    @CsvSource({
            "1, cert1, desc1, 10.0, 30, 2022-05-01T00:00:00Z, 2022-05-01T01:00:00Z",
            "2, cert2, desc2, 20.0, 60, 2022-06-01T00:00:00Z, 2022-06-01T01:00:00Z",
            "3, cert3, desc3, 30.0, 90, 2022-07-01T00:00:00Z, 2022-07-01T01:00:00Z"
    })
    void toEntityShouldMapDtoToEntity(
            Long id, String name, String description, BigDecimal price,
            Integer duration, String createDate, String lastUpdateDate) {
        CertificateDto dto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();

        Certificate entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertEquals(price, entity.getPrice());
        assertEquals(duration, entity.getDuration());
    }

    @DisplayName("Should map CertificateDto to Certificate entity")
    @ParameterizedTest(name = "{index} => dto={0}")
    @CsvSource({
            "1, Test Certificate, This is a test certificate., 100.0, 30",
            "2, Another Test Certificate, This is another test certificate., 50.0, 15"
    })
    void shouldMapCertificateDtoToCertificate(String id, String name, String description, BigDecimal price, Integer duration) {
        CertificateDto dto = CertificateDto.builder()
                .id(Long.valueOf(id))
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();

        Certificate certificate = mapper.toEntity(dto);
        assertEquals(dto.getId(), certificate.getId());
        assertEquals(dto.getName(), certificate.getName());
        assertEquals(dto.getDescription(), certificate.getDescription());
        assertEquals(dto.getPrice(), certificate.getPrice());
        assertEquals(dto.getDuration(), certificate.getDuration());
    }

    @DisplayName("Should map Certificate entity to CertificateDto")
    @ParameterizedTest(name = "{index} => entity={0}")
    @CsvSource({
            "1, Test Certificate, This is a test certificate., 100.0, 30",
            "2, Another Test Certificate, This is another test certificate., 50.0, 15"
    })
    void shouldMapCertificateToCertificateDto(String id, String name, String description, BigDecimal price, Integer duration) {
        Certificate certificate = Certificate.builder()
                .id(Long.valueOf(id))
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
        CertificateDto dto = mapper.toDto(certificate);

        assertEquals(certificate.getId(), dto.getId());
        assertEquals(certificate.getName(), dto.getName());
        assertEquals(certificate.getDescription(), dto.getDescription());
        assertEquals(certificate.getPrice(), dto.getPrice());
        assertEquals(certificate.getDuration(), dto.getDuration());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Tag1",
            "2, Tag2",
            "3, Tag3",
            "4, Tag4"
    })
    @DisplayName("map set of tag DTOs to set of tag entities")
    void testToTagSet(long id, String name) {
        TagDto tagDto = TagDto.builder()
                .id(id)
                .name(name)
                .build();
        Set<Tag> expected = Collections.singleton(Tag.builder().id(id).name(name).build());
        Set<Tag> actual = tagMapper.toTagSet(Collections.singleton(tagDto));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("map null or empty set of tag DTOs to null")
    void testToTagSetWithNullOrEmptyInput() {
        Set<Tag> expected = new HashSet<>();
        Set<Tag> actual = tagMapper.toTagSet(Collections.emptySet());
        assertEquals(expected, actual);
    }
}
