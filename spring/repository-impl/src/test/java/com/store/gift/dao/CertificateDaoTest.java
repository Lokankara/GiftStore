package com.store.gift.dao;

import com.store.gift.entity.Certificate;
import com.store.gift.entity.Tag;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.Subgraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class CertificateDaoTest {
    @Mock
    private EntityManagerFactory factory;
    @Mock
    private EntityTransaction transaction;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Tag> tagTypedQuery;
    @Mock
    private Query deleteOrderQuery;
    @Mock
    private Query deleteTokenQuery;
    @Mock
    private TypedQuery<Certificate> typedQuery;
    @Mock
    private EntityGraph<Certificate> graph;
    @Mock
    private TypedQuery<Tag> tagDtoTypedQuery;
    @Mock
    private Subgraph<Object> subgraph;
    @Mock
    private TypedQuery<Long> query;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private Root<Certificate> root;
    @Mock
    private Join<Object, Object> tagJoin;
    @Mock
    private CriteriaQuery<Certificate> criteriaQuery;
    @Mock
    private Path<Object> tagNamePath;
    private CertificateDao certificateDao;
    private final Long id = 1L;
    private final Certificate certificate = Certificate.builder().id(id).build();
    List<Tag> expectedTags = new ArrayList<>();

    @BeforeEach
    void setUp() {
        certificateDao = new CertificateDaoImpl(factory);
        expectedTags.add(Tag.builder().id(id).name("Tag").build());
        expectedTags.add(Tag.builder().id(id + 2).name("namespace").build());
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
//        when(criteriaBuilder.createQuery(Certificate.class)).thenReturn(criteriaQuery);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaQuery.from(Certificate.class)).thenReturn(root);
        when(factory.createEntityManager()).thenReturn(entityManager);
    }

//    @Test
//    @DisplayName("Given criteria and pageable, when findByCriteria is called, then it should return a list of certificates based on the criteria and pagination parameters")
//    void testFindByCriteria() {
//
//        Criteria criteria = Criteria.builder().build();
//        criteria.setTagNames(Arrays.asList("Tag1", "Tag2"));
//        criteria.setName("CertificateName");
//        criteria.setDescription("Description");
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
//
//        List<Certificate> certificates = new ArrayList<>();
//        certificates.add(new Certificate());
//        certificates.add(new Certificate());
//
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(Certificate.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(Certificate.class)).thenReturn(root);
//        when(root.join("tags", JoinType.INNER)).thenReturn(tagJoin);
//        when(tagJoin.get("name")).thenReturn(tagNamePath);
//        List<Predicate> predicates = new ArrayList<>();
//
//        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
//        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
//        when(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).thenReturn(any(Predicate.class));
//        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
//        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
//        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(certificates);
//
//        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
//        List<Certificate> result = certificateDao.findByCriteria(criteria, pageable);
//        assertEquals(2, result.size());
//    }

    @Test
    @DisplayName("Given a pageable request, when getAllBy is called, then it should return a list of certificates based on the pagination parameters")
    void testGetAllBy() {
        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Long> certificateIds = new ArrayList<>();
        certificateIds.add(1L);
        certificateIds.add(2L);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setHint(eq(Queries.FETCH_GRAPH), any(EntityGraph.class))).thenReturn(query);
        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(certificateIds);

        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
        when(typedQuery.setHint(eq(Queries.FETCH_GRAPH), any(EntityGraph.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(certificate, certificate));
        List<Certificate> result = certificateDao.getAllBy(pageable);
        assertEquals(2, result.size());
        assertEquals(certificate, result.get(0));
        assertEquals(certificate, result.get(1));
    }

    @Test
    void testFindAllByOrderId() {
        List<Certificate> expectedCertificates = Arrays.asList(certificate, certificate);
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(graph.addSubgraph(Queries.ORDERS)).thenReturn(subgraph);
        when(entityManager.createQuery(Queries.SELECT_CERTIFICATES_BY_ORDER_ID, Certificate.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.setHint(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedCertificates);
        Set<Certificate> actualCertificates = certificateDao.findAllByOrderId(id);
        assertEquals(new HashSet<>(expectedCertificates), actualCertificates);
        verify(entityManager).createQuery(Queries.SELECT_CERTIFICATES_BY_ORDER_ID, Certificate.class);
        verify(graph).addAttributeNodes(Queries.TAGS);
        verify(subgraph).addAttributeNodes(Queries.USER);
        verify(typedQuery).setParameter("orderId", id);
    }

    @Test
    @DisplayName("Test find Certificate by ID")
    void testFindById() {
        when(entityManager.find(eq(Certificate.class), eq(id), anyMap())).thenReturn(certificate);
        Certificate actualCertificate = certificateDao.findById(id);
        assertEquals(certificate, actualCertificate);
        verify(entityManager).close();
    }

    @DisplayName("Test update Certificate")
    @ParameterizedTest
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testUpdateCertificate(Long id, String name, String description, BigDecimal price, Integer duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        Certificate existed = getCertificate(id, "oldName", "oldDescription", BigDecimal.ONE, 10);
        when(entityManager.getReference(Certificate.class, id)).thenReturn(existed);
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(graph.addSubgraph(Queries.ORDERS)).thenReturn(subgraph);
        when(entityManager.find(eq(Certificate.class), eq(id), anyMap())).thenReturn(certificate);
        Certificate actualCertificate = certificateDao.update(certificate);
        assertEquals(certificate.getName(), actualCertificate.getName());
        assertEquals(certificate.getDescription(), actualCertificate.getDescription());
        assertEquals(certificate.getPrice(), actualCertificate.getPrice());
        assertEquals(certificate.getDuration(), actualCertificate.getDuration());
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).find(eq(Certificate.class), eq(id), anyMap());
        verify(transaction).commit();
    }

    @DisplayName("Test find certificate by ID")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 30",
            "2, Summer, Season 2, 45",
            "3, Spring, Season 3, 60",
            "4, Autumn, Season 4, 75"})
    void testFindById(Long id, String name, String description, Long id2) {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(Tag.builder().id(id).name(name).build());
        expectedTags.add(Tag.builder().id(id2).name(description).build());
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);
        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);
        assertEquals(expectedTags, actualTags);
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagTypedQuery).setParameter(anyString(), any());
        verify(tagTypedQuery).getResultList();
    }

    @DisplayName("Save certificate")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testSave(Long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        Tag tag = Tag.builder().name("Seasonal").build();
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        certificate.setTags(tags);
        when(entityManager.createQuery(Queries.SELECT_TAGS_BY_NAME, Tag.class)).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter("name", tag.getName())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(Collections.singletonList(tag));
        Certificate actualCertificate = certificateDao.save(certificate);
        assertEquals(certificate, actualCertificate);
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).createQuery(Queries.SELECT_TAGS_BY_NAME, Tag.class);
        verify(tagTypedQuery).setParameter("name", tag.getName());
        verify(tagTypedQuery).getResultList();
        verify(entityManager).persist(certificate);
        verify(transaction).commit();
    }

    @DisplayName("Get certificates by user ID")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}, Price: {3}, Duration: {4}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void getCertificatesByUserIdTest(Long id, String name, String description, BigDecimal price, int duration) {
        List<Certificate> expectedCertificates = Collections.singletonList(getCertificate(id, name, description, price, duration));
        Set<Long> certificateIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L));
        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anySet())).thenReturn(typedQuery);
        when(typedQuery.setHint(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedCertificates);
        certificateDao.findAllByIds(certificateIds);
        verify(factory).createEntityManager();
        verify(entityManager).createEntityGraph(Certificate.class);
        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
        verify(typedQuery).setParameter(anyString(), anySet());
        verify(typedQuery).setHint(anyString(), any());
        verify(typedQuery).getResultList();
    }

    @Test
    @DisplayName("Test update Certificate not found and verify rollback transaction")
    void testUpdateCertificateRollback() {
        when(entityManager.find(Certificate.class, id)).thenReturn(null);
        when(transaction.isActive()).thenReturn(true);
        assertThrows(PersistenceException.class, () -> certificateDao.update(certificate));
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(transaction).rollback();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Test save Entity not found")
    void testSaveCertificateNotFound() {
        when(transaction.isActive()).thenReturn(true);
        doThrow(new RuntimeException("Error")).when(transaction).commit();
        assertThrows(PersistenceException.class, () -> certificateDao.save(certificate));
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(transaction).rollback();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Test delete certificate and verify commit transaction")
    void testDeleteCertificate() {
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.find(eq(Certificate.class), eq(id), anyMap())).thenReturn(certificate);
        when(entityManager.createNativeQuery(Queries.DELETE_ORDER_CERTIFICATE)).thenReturn(deleteOrderQuery);
        when(deleteOrderQuery.setParameter("id", id)).thenReturn(deleteOrderQuery);
        when(entityManager.createNativeQuery(Queries.DELETE_CERTIFICATE)).thenReturn(deleteTokenQuery);
        when(deleteTokenQuery.setParameter("id", id)).thenReturn(deleteTokenQuery);
        when(entityManager.createNativeQuery(Queries.DELETE_CERTIFICATE_TAG)).thenReturn(deleteTokenQuery);
        when(deleteTokenQuery.setParameter("id", id)).thenReturn(deleteTokenQuery);
        certificateDao.delete(id);
        verify(transaction).begin();
        verify(entityManager).createNativeQuery(Queries.DELETE_ORDER_CERTIFICATE);
        verify(entityManager).createNativeQuery(Queries.DELETE_CERTIFICATE_TAG);
        verify(entityManager).createNativeQuery(Queries.DELETE_CERTIFICATE);
        verify(deleteOrderQuery).setParameter("id", id);
        verify(deleteOrderQuery).executeUpdate();
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Test find certificate by ID")
    void testFindCertificateById() {
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.find(eq(Certificate.class), eq(id), anyMap())).thenReturn(certificate);
        Certificate result = certificateDao.findById(id);
        assertEquals(certificate, result);
        verify(graph).addAttributeNodes("tags");
    }

    @ParameterizedTest(name = "Test #{index} - Name: {0}")
    @CsvSource({
            "Winter",
            "Summer",
            "Spring",
            "Autumn"})
    void testGetByNameCertificateNotFound(String name) {
        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());
        Optional<Certificate> result = certificateDao.findByUsername(name);
        assertFalse(result.isPresent());
        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
        verify(typedQuery).setParameter(anyString(), any());
        verify(typedQuery).getResultList();
    }

    @DisplayName("Test delete Entity not found and verify rollback transaction")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testGetByNameCertificateFound(Long id, String name, String description, BigDecimal price, int duration) {
        Certificate certificate = getCertificate(id, name, description, price, duration);
        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(certificate));
        Optional<Certificate> result = certificateDao.findByUsername(name);
        assertTrue(result.isPresent());
        assertEquals(certificate, result.get());
        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
        verify(typedQuery).setParameter(anyString(), any());
        verify(typedQuery).getResultList();
    }

    @Test
    @DisplayName("Test delete Tag not found and verify rollback transaction and Throws Exception")
    void testDeleteThrowsException() {
        when(transaction.isActive()).thenReturn(true);
        when(entityManager.find(Certificate.class, id)).thenReturn(null);
        doThrow(new RuntimeException("Error during remove")).when(entityManager).remove(any());
        assertThrows(PersistenceException.class, () -> certificateDao.delete(id));
        verify(transaction).rollback();
    }

    @Test
    @DisplayName("Test find tags by Certificate id")
    void testFindTagsByCertificateId() {
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);
        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);
        assertEquals(expectedTags, actualTags);
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagTypedQuery).setParameter(Queries.ID, id);
        verify(tagTypedQuery).getResultList();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter, 10, Season",
            "2, Summer, 20, Season",
            "3, Spring, 30, Season",
            "4, Autumn, 40, Season"
    })
    @DisplayName("Test find tags by Certificate id")
    void testFindTagsByCertificateId(Long id, String name, Long id2, String namespace) {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(Tag.builder().id(id).name(name).build());
        expectedTags.add(Tag.builder().id(id2).name(namespace).build());
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);
        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);
        assertEquals(expectedTags, actualTags);
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagTypedQuery).setParameter(anyString(), any());
        verify(tagTypedQuery).getResultList();
    }

    @Test
    @DisplayName("Populate expected Tag by Certificate id")
    void testFindTagsByCertificateIdExpected() {
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagDtoTypedQuery);
        when(tagDtoTypedQuery.setParameter(anyString(), any())).thenReturn(tagDtoTypedQuery);
        when(tagDtoTypedQuery.getResultList()).thenReturn(expectedTags);
        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);
        assertEquals(expectedTags, actualTags);
        verify(factory).createEntityManager();
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagDtoTypedQuery).setParameter(anyString(), any());
        verify(tagDtoTypedQuery).getResultList();
    }

    private static Certificate getCertificate(Long id, String name, String description, BigDecimal price, int duration) {
        return Certificate.builder().id(id).name(name)
                .description(description).price(price)
                .duration(duration).build();
    }
}
