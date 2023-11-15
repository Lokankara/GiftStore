package com.store.gift.repository.impl;

import com.store.gift.dao.UserDao;
import com.store.gift.entity.User;
import com.store.gift.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRepoImplTest {

    @Mock
    private UserRepository userRepository;
    private UserDao userDao;
    List<User> expectedUsers;
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("user").password("password").build();
        userRepository = mock(UserRepository.class);
        userDao = new UserRepoImpl(userRepository);
        expectedUsers = asList(
                User.builder().id(1L).username("user").password("password").build(),
                User.builder().id(2L).username("user2").password("password2").build()
        );
    }

    @Test
    @DisplayName("Given a pageable object, when getAllBy is called, then it should return a list of users")
    void getAllBy() {
        when(userRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(expectedUsers));
        List<User> actualUsers = userDao.getAllBy(Pageable.unpaged());
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    @DisplayName("Given an id, when getById is called, then it should return an optional containing the user with that id")
    void getById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        java.util.Optional<User> actualUser = userDao.getById(1L);
        assertEquals(java.util.Optional.of(user), actualUser);
    }

    @Test
    @DisplayName("Given a valid username, findByUsername should return the corresponding User entity")
    void testFindByUsername() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Optional<User> result = userDao.findByUsername(user.getUsername());
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
    }

    @Test
    @DisplayName("Given a valid User entity, save should persist it to the repository")
    void testSave() {
        when(userRepository.save(user)).thenReturn(user);
        User result = userDao.save(user);
        assertEquals(user, result);
    }
    @Test
    @DisplayName("Given an invalid username, findByUsername should throw a UsernameNotFoundException")
    void testFindByUsernameThrowsException() {
        String username = "invalid user";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDao.findByUsername(username));
    }

    @Test
    @DisplayName("Given a valid User entity, update should persist changes to the repository")
    void testUpdate() {
        user.setUsername("oldUsername");
        when(userRepository.save(user)).thenReturn(user);
        user.setUsername("newUsername");
        User result = userDao.update(user);
        assertEquals("newUsername", result.getUsername());
    }

    @Test
    @DisplayName("Given a valid User ID, delete should remove the corresponding User entity from the repository")
    void testDelete() {
        when(userRepository.save(user)).thenReturn(user);
        User saved = userRepository.save(user);
        assertNotNull(saved);
        Long id = saved.getId();
        assertNotNull(id);
        userDao.delete(id);
        Optional<User> result = userRepository.findById(id);
        assertTrue(result.isEmpty());
    }
}
