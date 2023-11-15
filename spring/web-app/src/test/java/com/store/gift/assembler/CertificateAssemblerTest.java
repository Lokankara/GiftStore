package com.store.gift.assembler;

import com.store.gift.controller.CertificateController;
import com.store.gift.dto.CertificateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class CertificateAssemblerTest {
    @Mock
    private CertificateController certificateController;
    @InjectMocks
    private CertificateAssembler certificateAssembler;
    CertificateDto certificateDto;

    @BeforeEach
    void setUp() {
        certificateDto = CertificateDto.builder()
                .id(1L)
                .name("Certificate 1")
                .description("Certificate 1 Description")
                .price(BigDecimal.TEN)
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(10)
                .tags(new HashSet<>())
                .build();
        certificateController = mock(CertificateController.class);
        certificateAssembler = new CertificateAssembler();
    }

    @ParameterizedTest
    @DisplayName("Test toModel method with single entity")
    @CsvSource({
            "1, Certificate 1, Certificate 1 Description, 10, 10",
            "2, 'Certificate 2', 'Certificate 2 Description',20, 20"
    })
    void toModel_returnsEntityModels(long id, String name, String description, int price, int duration) {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(BigDecimal.valueOf(price))
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(duration)
                .tags(new HashSet<>())
                .build();

        EntityModel<CertificateDto> entity = certificateAssembler.toModel(certificateDto);

        assertNotNull(entity);
        assertNotNull(entity.getContent());
        assertEquals(certificateDto, entity.getContent());
        assertNotNull(entity.getLink("self"));
        assertNotNull(entity.getLink("tags"));
        assertNotNull(entity.getLink("delete"));
    }


    @Test
    void toModelReturnsEntityModel() {

        EntityModel<CertificateDto> result = certificateAssembler.toModel(certificateDto);
        assertEquals(certificateDto, result.getContent());
        assertNotNull(result.getLink("self"));
        assertNotNull(result.getLink("tags"));
        assertNotNull(result.getLink("delete"));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'Certificate 1', 'Certificate 1 Description', 10",
            "2, 'Certificate 2', 'Certificate 2 Description', 20"
    })
    void toCollectionModelReturnsCollectionModel(long id, String name, String description, int duration) {
        CertificateDto certificateDto = CertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(BigDecimal.TEN)
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(duration)
                .tags(new HashSet<>())
                .build();

        Iterable<CertificateDto> certificateDtos = () -> singletonList(certificateDto).iterator();
        when(certificateController.getAll(PageRequest.of(0, 25,
                Sort.by(Sort.Direction.ASC, "id"))))
                .thenReturn(CollectionModel.of(
                        certificateAssembler.toCollectionModel(certificateDtos),
                        linkTo(methodOn(CertificateController.class)
                                .getAll(PageRequest.of(0, 25,
                                        Sort.by(Sort.Direction.ASC, "id"))))
                                .withSelfRel()
                ));

        CollectionModel<EntityModel<CertificateDto>> result =
                certificateController.getAll(PageRequest.of(
                        0, 25, Sort.by(Sort.Direction.ASC, "id")));

        assertEquals(1, result.getContent().size());
        assertNotNull(result.getLink("self"));
    }

    @Test
    void toCollectionModelReturnsCollectionModel() {

        CertificateDto certificateDto2 = CertificateDto.builder()
                .id(2L)
                .name("Certificate 2")
                .description("Certificate 2 Description")
                .price(BigDecimal.TEN)
                .createDate(new Timestamp(System.currentTimeMillis()))
                .lastUpdateDate(new Timestamp(System.currentTimeMillis()))
                .duration(10)
                .tags(new HashSet<>())
                .build();
        Iterable<CertificateDto> certificateDtos = Arrays.asList(certificateDto, certificateDto2);
        CollectionModel<EntityModel<CertificateDto>> result = certificateAssembler.toCollectionModel(certificateDtos);
        assertEquals(2, result.getContent().size());
        assertNotNull(result.getLink("self"));
    }
}
