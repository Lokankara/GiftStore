package com.store.gift.service;

import com.store.gift.dao.CertificateDao;
import com.store.gift.dao.CertificateDaoImpl;
import com.store.gift.dto.CertificateDto;
import com.store.gift.dto.CertificateSlimDto;
import com.store.gift.dto.PatchCertificateDto;
import com.store.gift.dto.TagDto;
import com.store.gift.entity.Certificate;
import com.store.gift.entity.Criteria;
import com.store.gift.entity.Tag;
import com.store.gift.exception.CertificateAlreadyExistsException;
import com.store.gift.exception.CertificateNotFoundException;
import com.store.gift.mapper.CertificateMapper;
import com.store.gift.mapper.TagMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CertificateServiceTest {
    @Mock
    private CertificateDao certificateDao = mock(CertificateDaoImpl.class);
    @Mock
    private CertificateMapper certificateMapper = mock(CertificateMapper.class);
    @Mock
    private TagMapper tagMapper = mock(TagMapper.class);
    @InjectMocks
    private CertificateService service;
    public List<Certificate> certificates;
    Long id = 1L;
    Page<CertificateDto> certificateDtos;
    List<CertificateDto> certificateDtoList = new ArrayList<>();
    List<CertificateSlimDto> slimDtos = new ArrayList<>();
    Page<CertificateSlimDto> slim;
    Certificate certificate;
    CertificateDto certificateDto;
    Pageable pageable;
    List<Tag> tags = Arrays.asList(
            Tag.builder().name("tag1").build(),
            Tag.builder().name("tag2").build());
    List<Certificate> expectedCertificates = Arrays.asList(
            Certificate.builder().id(1L).name("certificate1").build(),
            Certificate.builder().id(2L).name("certificate2").build());

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
        service = new CertificateServiceImpl(certificateDao, certificateMapper, tagMapper);
        certificate = Certificate.builder().id(1L).name("Gift").name("Certificate").build();
        certificateDto = CertificateDto.builder()
                .id(1L).name("Test Certificate")
                .description("Test description")
                .duration(10)
                .price(BigDecimal.valueOf(100))
                .createDate(Timestamp.from(Instant.now()))
                .lastUpdateDate(Timestamp.from(Instant.now()))
                .build();

        CertificateSlimDto slimDto = CertificateSlimDto.builder()
                .id(1L).name("Gift")
                .description("Certificate")
                .duration(10)
                .price(BigDecimal.valueOf(100))
                .createDate(Timestamp.from(Instant.now()))
                .lastUpdateDate(Timestamp.from(Instant.now()))
                .build();
        slimDtos.add(slimDto);

        certificateDtoList.add(CertificateDto.builder().id(1L).name("Gift1")
                .description("Certificate1").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateDtoList.add(CertificateDto.builder().id(2L).name("Gift2")
                .description("Certificate2").duration(10)
                .price(BigDecimal.valueOf(100)).build());
        certificateDtoList.add(CertificateDto.builder().id(3L).name("Gift3")
                .description("Certificate3").duration(10)
                .price(BigDecimal.valueOf(100)).build());

        List<CertificateDto> certificateDtoList = Arrays.asList(
                CertificateDto.builder().id(1L).name("Winter").description("Certificate1").build(),
                CertificateDto.builder().id(2L).name("Summer").description("Certificate2").build(),
                CertificateDto.builder().id(3L).name("Spring").description("Certificate3").build(),
                CertificateDto.builder().id(3L).name("Autumn").description("Certificate3").build()
        );
        List<CertificateSlimDto> certificateSlimDtos = Arrays.asList(
                CertificateSlimDto.builder().id(1L).name("Winter").description("Certificate1").build(),
                CertificateSlimDto.builder().id(2L).name("Summer").description("Certificate2").build(),
                CertificateSlimDto.builder().id(3L).name("Spring").description("Certificate3").build(),
                CertificateSlimDto.builder().id(3L).name("Autumn").description("Certificate3").build()
        );

        slim = new PageImpl<>(certificateSlimDtos);
        certificateDtos = new PageImpl<>(certificateDtoList);
        certificates = new ArrayList<>();
        certificates.add(Certificate.builder().id(1L).name("Gift1").duration(10)
                .description("Certificate1").price(new BigDecimal(100))
                .tags(new HashSet<>()).build());
        certificates.add(Certificate.builder().id(2L).name("Gift2").duration(20)
                .description("Certificate2").price(new BigDecimal(200))
                .tags(new HashSet<>()).build());
        certificates.add(Certificate.builder().id(3L).name("Gift3")
                .description("Certificate3").duration(30).price(new BigDecimal(300))
                .tags(new HashSet<>()).build());
    }

    @Test
    @DisplayName("Given an order ID, when create a CertificateService instance with the mocked certificateDao, then return an empty Optional")
    void testGetByIdThrowsCertificateNotFoundException() {
        CertificateDao certificateDao = mock(CertificateDao.class);
        when(certificateDao.getById(id)).thenReturn(Optional.empty());
        try {
            service.getById(1L);
            Assertions.fail("Expected CertificateNotFoundException to be thrown");
        } catch (CertificateNotFoundException e) {
            assertEquals("Certificate not found with id: 1", e.getMessage());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given an order ID, when getByOrderId is called, then return the corresponding result")
    void testGetByOrderId(long id, String name, String description, BigDecimal price, int duration) {
        Certificate post = getCertificate(id, name, description, price, duration);
        when(certificateDao.findAllByOrderId(id))
                .thenReturn(new HashSet<>(Collections.singleton(post)));
        List<CertificateDto> actualCertificates = service.getByOrderId(id);
        assertEquals(certificateMapper.toDtoList(
                expectedCertificates), actualCertificates);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a valid CertificateDto, when save is called, then return the saved CertificateDto")
    void testSave(long id, String name, String description, BigDecimal price, int duration) {
        CertificateDto postDto = getCertificateDto(id, name, description, price, duration);
        Certificate expectedCertificate = certificateMapper.toEntity(postDto);
        when(certificateDao.save(expectedCertificate)).thenReturn(expectedCertificate);
        CertificateDto actualCertificate = service.save(postDto);
        assertEquals(certificateMapper.toDto(expectedCertificate), actualCertificate);
    }

    @ParameterizedTest
    @CsvSource({"1, 20", "2, 40", "3, 60", "4, 80"})
    @DisplayName("Given a pageable object, when findAllWithPageable is called, then return the paginated result")
    void testGetAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        when(certificateDao.getAllBy(pageable)).thenReturn(expectedCertificates);
        List<CertificateDto> actualCertificates = service.getCertificates(pageable);
        assertEquals(certificateMapper.toDtoList(expectedCertificates), actualCertificates);
        verify(certificateDao).getAllBy(pageable);
    }

    @ParameterizedTest(name = "Test #{index} - Certificate ID: {0}")
    @CsvSource({"1", "2", "3", "4"})
    @DisplayName("Given a valid certificate ID, when findTagsByCertificateId is called, then return the tags associated with the certificate")
    void testFindTagsByCertificate(Long certificateId) {
        Set<Tag> expectedTags = new HashSet<>(tags);
        Set<TagDto> expectedTagDtos = new HashSet<>(Arrays.asList(
                TagDto.builder().name("tag1").build(),
                TagDto.builder().name("tag2").build()));
        when(certificateDao.findTagsByCertificateId(certificateId)).thenReturn(tags);
        when(tagMapper.toDtoSet(expectedTags)).thenReturn(expectedTagDtos);
        Set<TagDto> actualTagDtos = service.findTagsByCertificateId(certificateId);
        assertEquals(expectedTagDtos, actualTagDtos);
        verify(certificateDao).findTagsByCertificateId(certificateId);
        verify(tagMapper).toDtoSet(expectedTags);
    }


    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @DisplayName("Given a valid certificate ID, when getCertificateById is called, then return the corresponding Certificate")
    void testGetById(Long id, String name) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).name(name).build();
        Certificate certificate = Certificate.builder().id(id).name(name).build();
        when(certificateDao.getById(id)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = service.getById(id);
        assertEquals(certificateDto, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a valid certificate name, when getCertificateByName is called, then return the corresponding Certificate")
    void getByName(Long id, String name, String description, BigDecimal price, int duration) {
        CertificateDto certificateDto = getCertificateDto(id, name, description, price, duration);
        Optional<Certificate> certificate = Optional.of(getCertificate(id, name, description, price, duration));
        when(certificateDao.findByUsername(name)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate.get())).thenReturn(certificateDto);
        CertificateDto result = service.getByName(name);
        assertThat(result).usingRecursiveComparison().isEqualTo(certificateDto);
        assertThat(result).isNotNull().isInstanceOf(CertificateDto.class);
        assertEquals(certificateDto, result);
        verify(certificateDao).findByUsername(name);
        verify(certificateMapper).toDto(certificate.get());
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    @DisplayName("Given a valid certificate ID, when deleteCertificateById is called and the certificate is found, then delete the certificate")
    void deleteFound(Long id) {
        CertificateDto certificateDto = CertificateDto.builder().id(id).build();
        Certificate certificate = Certificate.builder().id(id).build();
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        service.delete(id);
        verify(certificateDao).delete(id);
    }

    @ParameterizedTest
    @CsvSource({"Winter, 1, 25", "Summer, 2, 50", "Spring, 3, 75", "Autumn, 4, 100"})
    @DisplayName("Given no tags, when getAllCertificatesWithoutTags is called, then return all certificates without tags")
    void testGetAllCertificatesWithoutTags(String tagName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, tagName));
        when(certificateDao.getAllBy(pageable)).thenReturn(certificates);
        when(certificateMapper.toCertificateSlimDto(certificates)).thenReturn(slimDtos);
        List<CertificateDto> result = service.getCertificates(pageable);
        result.forEach(dto -> assertThat(dto).isNotNull().isInstanceOf(CertificateDto.class));
        when(certificateDao.getAllBy(pageable)).thenReturn(certificates);
        when(certificateMapper.toCertificateSlimDto(certificates)).thenReturn(slimDtos);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Given an existing certificate ID and updated data, when updateCertificate is called, then update the certificate")
    void testUpdateShouldUpdateCertificate() {
        when(certificateDao.getById(certificateDtos.getContent().get(0).getId())).thenReturn(Optional.of(certificates.get(0)));
        when(certificateMapper.toEntity(certificateDtos.getContent().get(0))).thenReturn(certificates.get(0));
        when(certificateDao.update(certificates.get(0))).thenReturn(certificate);
        assertNotNull(certificateDao.update(certificateMapper.toEntity(certificateDtos.getContent().get(0))));
        verify(certificateMapper, times(1)).toEntity(certificateDtos.getContent().get(0));
        verify(certificateDao, times(1)).update(certificates.get(0));
    }

    @Test
    @DisplayName("Given a valid CertificateDto, when build is called, then return a new Certificate entity")
    void testCertificateDtoBuilder() {
        assertEquals(Long.valueOf(1L), certificateDto.getId());
        assertEquals("Test Certificate", certificateDto.getName());
        assertEquals("Test description", certificateDto.getDescription());
        assertEquals(Integer.valueOf(10), certificateDto.getDuration());
        assertEquals(BigDecimal.valueOf(100), certificateDto.getPrice());
        assertThat(certificateDto).isNotNull().isInstanceOf(CertificateDto.class);
        assertNotNull(certificateDto.getCreateDate());
        assertNotNull(certificateDto.getLastUpdateDate());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a certificate name, when getCertificateByName is called, then return the corresponding certificate")
    void getCertificateByName(Long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        CertificateDto certificateDto = getCertificateDto(id, name, description, price, duration);
        when(certificateDao.findByUsername(name)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = service.getByName(name);
        assertThat(result).usingRecursiveComparison().isEqualTo(certificateDto);
        assertThat(result).isNotNull().isInstanceOf(CertificateDto.class);
        verify(certificateDao, times(1)).findByUsername(name);
        verify(certificateMapper, times(1)).toDto(certificate);
        assertEquals(certificateDto, result);
    }

    @Test
    @DisplayName("Given a non-existent certificate name, when getCertificateByName is called, then return null or throw an exception")
    void getCertificateByNonExistentName() {
        when(certificateDao.findByUsername("NonExistentCertificate"))
                .thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () ->
                service.getByName("NonExistentCertificate"));
        verify(certificateDao, times(1))
                .findByUsername("NonExistentCertificate");
    }

    @ParameterizedTest(name = "Run {index}: name = {0}")
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a valid certificate name, when getCertificateByName is called, then return the corresponding Certificate")
    void getCertificateByNames(Long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        CertificateDto certificateDto = getCertificateDto(id, name, description, price, duration);
        when(certificateDao.findByUsername(name)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto dto = service.getByName(name);
        assertNotNull(certificateDto);
        assertEquals(name, certificateDto.getName());
        assertEquals("description", certificateDto.getDescription());
        assertEquals(new BigDecimal("10"), certificateDto.getPrice());
        assertEquals(30, certificateDto.getDuration());
        assertEquals(certificateDto, dto);
        verify(certificateDao, times(1)).findByUsername(name);
        verify(certificateMapper, times(1)).toDto(certificate);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a valid user ID, when getCertificatesByUserId is called, then return the list of certificates associated with the user")
    void getCertificatesByUserIdTest(
            long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        CertificateDto certificateDto = getCertificateDto(id, name, description, price, duration);
        List<Certificate> certificates = Collections.singletonList(certificate);
        List<CertificateDto> expectedDtos = Collections.singletonList(certificateDto);
        when(certificateDao.getCertificatesByUserId(anyLong())).thenReturn(certificates);
        when(certificateMapper.toDtoList(Collections.singletonList(certificate))).thenReturn(expectedDtos);
        Page<CertificateDto> actualCertificates = service.getCertificatesByUserId(id);
        assertThat(actualCertificates).isNotNull().isInstanceOf(PageImpl.class);
        assertEquals(expectedDtos, actualCertificates.getContent());
    }

    @ParameterizedTest(name = "Update {index}: name = {1}")
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a valid PatchCertificateDto, when update is called, then return the updated CertificateDto")
    void testUpdateCertificates(Long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        CertificateDto dto = getCertificateDto(id, name, description, price, duration);
        PatchCertificateDto patchDto = PatchCertificateDto.builder()
                .id(id).duration(duration).price(price).build();
        when(certificateDao.update(certificate)).thenReturn(certificate);
        when(certificateMapper.toEntity(patchDto)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(dto);
        CertificateDto result = service.update(patchDto);
        verify(certificateDao).update(certificate);
        verify(certificateMapper).toEntity(patchDto);
        verify(certificateMapper).toDto(certificate);
        assertThat(result).isNotNull().isInstanceOf(CertificateDto.class);
        assertEquals(dto, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a set of valid certificate IDs, when findAllByIds is called, then return the corresponding set of Certificates")
    void getByIdsTest(long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = getCertificate(id, name, description, price, duration);
        CertificateDto certificateDto = getCertificateDto(id, name, description, price, duration);
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        List<Certificate> certificates = Collections.singletonList(certificate);
        List<CertificateDto> expectedDtos = Collections.singletonList(certificateDto);
        when(certificateDao.findAllByIds(ids)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(expectedDtos);
        List<CertificateDto> actualCertificateDtos = service.getByIds(ids);
        assertThat(actualCertificateDtos).usingRecursiveComparison().isEqualTo(expectedDtos);
        actualCertificateDtos.forEach(actualCertificateDto -> assertThat(actualCertificateDto).isNotNull().isInstanceOf(CertificateDto.class));
        assertEquals(new HashSet<>(expectedDtos), new HashSet<>(actualCertificateDtos));
        verify(certificateDao).findAllByIds(ids);
        verify(certificateMapper).toDtoList(certificates);
    }

    @Test
    @DisplayName("Given a Pageable object, when getCertificates method is called, then a list of CertificateDto is returned")
    void testGetCertificates() {
        List<CertificateDto> certificateDtos = Arrays.asList(mock(CertificateDto.class), mock(CertificateDto.class));
        when(certificateDao.getAllBy(pageable)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtos);
        List<CertificateDto> result = service.getCertificates(pageable);
        assertThat(result).usingRecursiveComparison().isEqualTo(certificateDtos);
        result.forEach(certificateDto -> assertThat(certificateDto).isNotNull().isInstanceOf(CertificateDto.class));
        assertSame(certificateDtos, result);
        verify(certificateDao).getAllBy(pageable);
        verify(certificateMapper).toDtoList(certificates);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30",
            "2, SQL, description, 10, 30",
            "3, Programming, description, 10, 30",
            "4, PorsgreSQL, description, 10, 30",
            "5, Spring, description, 10, 30"
    })
    @DisplayName("Given a CertificateDto, when save method is called, then a CertificateDto is returned")
    void testSaveCertificate(long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        CertificateDto savedDto = getCertificateDto(id, name, description, price, duration);
        CertificateDto dto = getCertificateDto(id, name, description, price, duration);
        when(certificateDao.findByUsername(certificate.getName())).thenReturn(Optional.empty());
        when(certificateMapper.toEntity(dto)).thenReturn(certificate);
        when(certificateDao.save(certificate)).thenReturn(certificate);
        when(certificateMapper.toDto(certificate)).thenReturn(savedDto);
        CertificateDto result = service.save(dto);
        assertThat(result).usingRecursiveComparison().isEqualTo(savedDto);
        assertThat(result).isNotNull().isInstanceOf(CertificateDto.class);
        verify(certificateDao).findByUsername(certificate.getName());
        verify(certificateMapper).toEntity(dto);
        verify(certificateDao).save(certificate);
        verify(certificateMapper).toDto(certificate);
    }

    @Test
    @DisplayName("Given a CertificateDto with a name that already exists, when save method is called, then a CertificateAlreadyExistsException is thrown")
    void testSaveCertificateWhenCertificateAlreadyExists() {
        CertificateDto dto = mock(CertificateDto.class);
        when(dto.getName()).thenReturn("certificateName");
        when(certificateDao.findByUsername("certificateName")).thenReturn(Optional.of(certificate));
        CertificateAlreadyExistsException exception = assertThrows(CertificateAlreadyExistsException.class, () -> service.save(dto));
        assertThat(exception).hasMessageContaining("Certificate already exists with name certificateName");
        verify(certificateDao).findByUsername("certificateName");
    }

    @Test
    @DisplayName("Given a list of tag names, when findAllBy is called, then retrieve all certificates associated with the tag names")
    void testFindAllBy() {
        Criteria criteria = Criteria.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        when(certificateDao.findByCriteria(criteria, pageable)).thenReturn(certificates);
        when(certificateMapper.toDtoList(certificates)).thenReturn(certificateDtoList);
        List<CertificateDto> result = service.findAllBy(criteria, pageable);
        verify(certificateDao).findByCriteria(criteria, pageable);
        verify(certificateMapper).toDtoList(certificates);
        result.forEach(dto -> assertThat(dto).usingRecursiveComparison().isNotNull().isInstanceOf(CertificateDto.class));
    }

    @Test
    @DisplayName("Given a valid certificate ID, when getById is called, then return the corresponding CertificateDto")
    void testGetById() {
        when(certificateDao.getById(id)).thenReturn(Optional.of(certificate));
        when(certificateMapper.toDto(certificate)).thenReturn(certificateDto);
        CertificateDto result = service.getById(id);
        assertThat(result).usingRecursiveComparison().isEqualTo(certificateDto);
        assertThat(result).isNotNull().isInstanceOf(CertificateDto.class);
        assertEquals(certificateDto, result);
        verify(certificateMapper).toDto(certificate);
        verify(certificateDao).getById(id);
    }

    private static CertificateDto getCertificateDto(
            long id, String name, String description, BigDecimal price, int duration) {
        return CertificateDto.builder()
                .id(id).name(name).description(description)
                .duration(duration).price(price).build();
    }

    private static Certificate getCertificate(
            long id, String name, String description, BigDecimal price, int duration) {
        return Certificate.builder().id(id).description(description)
                .duration(duration).price(price).name(name).build();
    }
}
