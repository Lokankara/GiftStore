package com.store.gift.dao;

import com.store.gift.entity.Certificate;
import com.store.gift.entity.Order;
import com.store.gift.entity.Role;
import com.store.gift.entity.RoleType;
import com.store.gift.entity.User;
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
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {
    @Mock
    private CriteriaQuery<Role> roleCriteriaQuery;
    private UserDaoImpl userDao;
    @Mock
    private EntityManagerFactory factory;
    @Mock
    private EntityTransaction transaction;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<User> typedQuery;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<User> criteriaQuery;
    @Mock
    Subgraph<Order> orderGraph;
    @Mock
    Subgraph<Certificate> certificateGraph;
    @Mock
    Subgraph<Role> roleGraph;
    @Mock
    private Root<User> root;
    @Mock
    private EntityGraph<User> graph;
    @Mock
    TypedQuery<Role> roleQuery;
    @Mock
    Root<Role> roleRoot;
    @Mock
    private Query deleteOrderQuery;
    @Mock
    private Query deleteTokenQuery;
    @Mock
    private Query deleteUserQuery;
    @Mock
    private CriteriaUpdate<User> criteriaUpdate;
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    private final Long id = 1L;
    private final User user = User.builder().id(id).username("Java").email("spring@i.ua").password("Spring").build();

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(factory);
        when(factory.createEntityManager()).thenReturn(entityManager);
    }

    @Test
    @DisplayName("Given an ID, when delete method is called, then the user with the given ID is deleted from the database")
    void testDeleteUserById() {
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createNativeQuery(Queries.DELETE_ORDER)).thenReturn(deleteOrderQuery);
        when(deleteOrderQuery.setParameter("id", id)).thenReturn(deleteOrderQuery);
        when(entityManager.createNativeQuery(Queries.DELETE_TOKEN)).thenReturn(deleteTokenQuery);
        when(deleteTokenQuery.setParameter("id", id)).thenReturn(deleteTokenQuery);
        when(entityManager.createNativeQuery(Queries.DELETE_USER)).thenReturn(deleteUserQuery);
        when(deleteUserQuery.setParameter("id", id)).thenReturn(deleteUserQuery);
        userDao.delete(id);
        verify(transaction).begin();
        verify(deleteOrderQuery).executeUpdate();
        verify(deleteTokenQuery).executeUpdate();
        verify(deleteUserQuery).executeUpdate();
        verify(transaction).commit();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Given an ID, when delete method is called and an error occurs, then a PersistenceException is thrown")
    void testDeleteWithPersistenceException() {
        when(entityManager.getTransaction()).thenReturn(transaction);
        doThrow(new RuntimeException()).when(transaction).begin();
        assertThrows(PersistenceException.class, () -> userDao.delete(1L));
    }

    @ParameterizedTest
    @DisplayName("When getAll method is called, then a list of all users is returned")
    @CsvSource({
            "1, Olivia, Noah, Olivia@i.ua, Noah@gmail.com",
            "2, Emma, Liam, Emma@i.ua, Liam@gmail.com",
            "3, Charlotte, Oliver, Charlotte@i.ua, Oliver@gmail.com",
            "4, Amelia, Elijah, Amelia@i.ua, Elijah@gmail.com",
            "5, Ava, Leo, Ava@i.ua, Leo@gmail.com"})
    void testGetAllUsers(Long id1, String username, String password, String email1, String email2) {
        List<User> users = Arrays.asList(getUser(id1, username, password, email1),
                getUser(id1 + 10, username, password, email2));
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(root);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(entityManager.createEntityGraph(User.class)).thenReturn(graph);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.setHint(Queries.FETCH_GRAPH, graph)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(users);
        doNothing().when(orderGraph).addAttributeNodes("certificates");
        doNothing().when(certificateGraph).addAttributeNodes("tags");
        doNothing().when(roleGraph).addAttributeNodes("authorities");
        doReturn(orderGraph).when(graph).addSubgraph("orders");
        doReturn(certificateGraph).when(orderGraph).addSubgraph("certificates");
        doReturn(roleGraph).when(graph).addSubgraph("role");
        List<User> result = userDao.getAllBy(pageable);
        assertEquals(users, result);
        verify(factory).createEntityManager();
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(User.class);
        verify(criteriaQuery).from(User.class);
        verify(entityManager).createEntityGraph(User.class);
        verify(criteriaQuery).select(root);
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).setHint(Queries.FETCH_GRAPH, graph);
        verify(typedQuery).getResultList();
        verify(entityManager).close();
    }

    @ParameterizedTest
    @DisplayName("Given a name, when getUserByName method is called and a user is found, then an Optional containing the user entity is returned")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com",
            "2, Emma, Liam, Emma-Liam@gmail.com",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com",
            "5, Ava, Leo, Ava-Leo@gmail.com"})
    void testGetByName(Long userId, String firstName, String password, String email) {
        User user = getUser(userId, firstName, password, email);
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(Queries.SELECT_USER_BY_NAME, User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", user.getUsername())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(user));
        Optional<User> result = userDao.findByUsername(user.getUsername());
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(entityManager).createQuery(Queries.SELECT_USER_BY_NAME, User.class);
        verify(typedQuery).setParameter("name", user.getUsername());
        verify(typedQuery).getResultList();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Given a name, when getUserByName method is called and no user is found, then an empty Optional is returned")
    void testGetByNameNoUserFound() {
        String name = "User";
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(Queries.SELECT_USER_BY_NAME, User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", name)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());
        Optional<User> result = userDao.findByUsername(name);
        assertFalse(result.isPresent());
        verify(entityManager).createQuery(Queries.SELECT_USER_BY_NAME, User.class);
        verify(typedQuery).setParameter("name", name);
        verify(typedQuery).getResultList();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Given a user entity, when save method is called, then the user is persisted to the database")
    void testSaveUser() {
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery(Queries.SELECT_USER_BY_NAME, User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", user.getUsername())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());
        User savedUser = userDao.save(user);
        assertEquals(user, savedUser);
        verify(entityManager).createQuery(Queries.SELECT_USER_BY_NAME, User.class);
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(typedQuery).setParameter("name", user.getUsername());
        verify(typedQuery).getResultList();
        verify(entityManager).persist(user);
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Given a user entity, when save method is called and an error occurs, then a PersistenceException is thrown")
    void testSaveWithUserAlreadyExistsException() {
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery(Queries.SELECT_USER_BY_NAME, User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", user.getUsername())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(user));
        when(transaction.isActive()).thenReturn(true);
        assertThrows(PersistenceException.class, () -> userDao.save(user));
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).createQuery(Queries.SELECT_USER_BY_NAME, User.class);
        verify(typedQuery).setParameter("name", user.getUsername());
        verify(typedQuery).getResultList();
        verify(transaction).rollback();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Given a user entity, when update method is called, then the user is updated in the database")
    void testUpdate() {
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createCriteriaUpdate(User.class)).thenReturn(criteriaUpdate);
        when(criteriaUpdate.from(User.class)).thenReturn(root);
        when(entityManager.createEntityGraph(User.class)).thenReturn(graph);
        when(entityManager.createQuery(criteriaUpdate)).thenReturn(typedQuery);
        when(typedQuery.setHint(Queries.FETCH_GRAPH, graph)).thenReturn(typedQuery);
        when(entityManager.find(User.class, id)).thenReturn(mock(User.class));
        when(entityManager.find(User.class, id)).thenAnswer(invocation -> user);
        doNothing().when(orderGraph).addAttributeNodes(Queries.CERTIFICATES);
        doNothing().when(certificateGraph).addAttributeNodes("tags");
        doNothing().when(roleGraph).addAttributeNodes("authorities");
        doReturn(orderGraph).when(graph).addSubgraph("orders");
        doReturn(certificateGraph).when(orderGraph).addSubgraph(Queries.CERTIFICATES);
        doReturn(roleGraph).when(graph).addSubgraph("role");
        User updatedUser = userDao.update(user);
        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getUsername(), updatedUser.getUsername());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        verify(factory).createEntityManager();
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createCriteriaUpdate(User.class);
        verify(criteriaUpdate).from(User.class);
        verify(criteriaUpdate).set(root.get("username"), user.getUsername());
        verify(criteriaUpdate).set(root.get("email"), user.getEmail());
        verify(criteriaUpdate).set(root.get("password"), user.getPassword());
        verify(entityManager).createEntityGraph(User.class);
        verify(entityManager).createQuery(criteriaUpdate);
        verify(typedQuery).setHint(Queries.FETCH_GRAPH, graph);
        verify(typedQuery).executeUpdate();
        verify(entityManager).find(User.class, 1L);
    }

    @Test
    @DisplayName("Test that update method updates user and role correctly")
    void testUpdateUserAndRoleCorrectly() {
        when(entityManager.getTransaction()).thenReturn(transaction);
        Role role = Role.builder().permission(RoleType.ADMIN).build();
        user.setRole(role);
        Role existingRole = Role.builder().id(2L).permission(RoleType.ADMIN).build();
        when(roleCriteriaQuery.from(Role.class)).thenReturn(roleRoot);
        when(criteriaUpdate.from(User.class)).thenReturn(root);
        when(criteriaBuilder.createCriteriaUpdate(User.class)).thenReturn(criteriaUpdate);
        when(criteriaBuilder.createQuery(Role.class)).thenReturn(roleCriteriaQuery);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(entityManager.createEntityGraph(User.class)).thenReturn(graph);
        when(entityManager.createQuery(roleCriteriaQuery)).thenReturn(roleQuery);
        when(entityManager.createQuery(criteriaUpdate)).thenReturn(typedQuery);
        when(entityManager.find(User.class, 1L)).thenAnswer(invocation -> user);
        when(typedQuery.setHint(Queries.FETCH_GRAPH, graph)).thenReturn(typedQuery);
        when(roleQuery.setMaxResults(1)).thenReturn(roleQuery);
        when(roleQuery.getResultList()).thenReturn(Collections.singletonList(existingRole));
        doNothing().when(orderGraph).addAttributeNodes(Queries.CERTIFICATES);
        doNothing().when(certificateGraph).addAttributeNodes("tags");
        doNothing().when(roleGraph).addAttributeNodes("authorities");
        doReturn(orderGraph).when(graph).addSubgraph("orders");
        doReturn(roleGraph).when(graph).addSubgraph("role");
        doReturn(certificateGraph).when(orderGraph).addSubgraph(Queries.CERTIFICATES);

        User updatedUser = userDao.update(user);
        assertNotNull(updatedUser);
        assertEquals(id, updatedUser.getId());
        assertEquals(user.getUsername(), updatedUser.getUsername());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        verify(factory).createEntityManager();
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(criteriaBuilder).createCriteriaUpdate(User.class);
        verify(criteriaUpdate).from(User.class);
        verify(criteriaUpdate).set(root.get("username"), user.getUsername());
        verify(criteriaUpdate).set(root.get("email"), user.getEmail());
        verify(criteriaUpdate).set(root.get("password"), user.getPassword());
        verify(criteriaUpdate).where(criteriaBuilder.equal(root.get("id"), user.getId()));
        verify(entityManager).createEntityGraph(User.class);
        verify(entityManager).createQuery(criteriaUpdate);
        verify(typedQuery).setHint(Queries.FETCH_GRAPH, graph);
        verify(typedQuery).executeUpdate();
        verify(entityManager).find(User.class, id);
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Test that given an ID, getById method returns an Optional containing the user entity")
    void testGetUserById() {
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createEntityGraph(User.class)).thenReturn(graph);
        when(entityManager.find(User.class, id, Collections.singletonMap(Queries.FETCH_GRAPH, graph))).thenReturn(user);
        Optional<User> result = userDao.getById(id);
        assertTrue(result.isPresent());
        assertNotNull(result);
        verify(factory).createEntityManager();
        verify(entityManager).createEntityGraph(User.class);
        verify(entityManager).find(User.class, 1L, Collections.singletonMap(Queries.FETCH_GRAPH, graph));
    }

    private static User getUser(Long userId, String firstName, String password, String email) {
        return User.builder()
                .id(userId)
                .username(firstName + "-" + password)
                .password(password)
                .email(email)
                .build();
    }
}
