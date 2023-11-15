package com.store.gift.service;

import com.store.gift.dao.UserDao;
import com.store.gift.dto.RoleDto;
import com.store.gift.dto.UserDto;
import com.store.gift.dto.UserSlimDto;
import com.store.gift.entity.Certificate;
import com.store.gift.entity.Order;
import com.store.gift.entity.Role;
import com.store.gift.entity.RoleType;
import com.store.gift.entity.User;
import com.store.gift.exception.UserNotFoundException;
import com.store.gift.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDao userDao;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserServiceImpl userService;
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    private final Long id = 1L;

    @ParameterizedTest
    @DisplayName("Given a UserSlimDto, when save method is called, then a UserDto is returned")
    @CsvSource({
            "1, Olivia, password1, olivia@example.com, GUEST",
            "2, Emma, password2, emma@example.com, USER",
            "3, Charlotte, password3, charlotte@example.com, ADMIN"
    })
    void testSaveUser(Long userId, String password, String username, String email, String role) {
        UserSlimDto dto = getUserSlimDto(userId, password, username, email, role);
        User user = getUser(userId, password, username, email, role);
        UserDto userDto = getUserDto(userId, username, email);

        when(mapper.toEntity(dto)).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(userDto);


        UserDto result = userService.save(dto);

        assertSame(userDto, result);
        verify(mapper).toEntity(dto);
        verify(userDao).save(user);
        verify(mapper).toDto(user);
    }

    @ParameterizedTest
    @DisplayName("When getAll method is called, then a list of all users is returned")
    @CsvSource({
            "1, Olivia, Noah, Olivia@i.ua, Noah@gmail.com",
            "2, Emma, Liam, Emma@i.ua, Liam@gmail.com",
            "3, Charlotte, Oliver, Charlotte@i.ua, Oliver@gmail.com",
            "4, Amelia, Elijah, Amelia@i.ua, Elijah@gmail.com",
            "5, Ava, Leo, Ava@i.ua, Leo@gmail.com"
    })
    void getAllTest(Long id1, String username1, String username2, String email1, String email2) {
        List<User> users = Arrays.asList(
                User.builder().id(id1).username(username1).email(email1).orders(new HashSet<>()).build(),
                User.builder().id(id1 + 10).username(username2).email(email2).orders(new HashSet<>()).build());
        List<UserDto> expectedDtos = Arrays.asList(
                UserDto.builder().id(id1).username(username1).email(email1).build(),
                UserDto.builder().id(id1 + 1).username(username2).email(email2).build());
        when(userDao.getAllBy(pageable)).thenReturn(users);
        when(mapper.toDtoList(users)).thenReturn(expectedDtos);
        assertEquals(expectedDtos, userService.getAll(pageable).getContent());
        verify(userDao).getAllBy(pageable);
        verify(mapper).toDtoList(users);
        verifyNoMoreInteractions(userDao, mapper);
    }

    @ParameterizedTest
    @DisplayName("Given an ID, when findById method is called and a user is found, then an Optional containing the user entity is returned")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com",
            "2, Emma, Liam, Emma-Liam@gmail.com",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com",
            "5, Mia, Aiden, Mia-Aiden@gmail.com"
    })
    void testFindUserById(Long id, String firstName, String lastName, String email) {
        User expectedUser = getUser(id, firstName, lastName, email, "USER");
        Optional<User> optionalUser = Optional.of(expectedUser);
        when(userDao.getById(id)).thenReturn(optionalUser);
        assertEquals(expectedUser, userService.findById(id));
        verify(userDao).getById(id);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("Given an ID, when findById method is called and no user is found, then an empty Optional is returned")
    void testFindUserByIdWhenUserNotFound() {
        Optional<User> optionalUser = Optional.empty();
        when(userDao.getById(id)).thenReturn(optionalUser);
        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
        verify(userDao).getById(id);
        verifyNoMoreInteractions(userDao);
    }

    @ParameterizedTest
    @DisplayName("Given an ID and an order, when findByIdWithOrder method is called and a user is found, then an Optional containing the user entity with the specified order is returned")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void testFindUserByIdWithOrder(Long userId, String firstName, String lastName, String email,
                                   long certificateId, String name, String description,
                                   BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder().id(certificateId).name(name).description(description).duration(duration).build();
        Order order = Order.builder().id(certificateId).cost(price).certificates(Collections.singleton(certificate)).build();
        User expectedUser = getUser(userId, firstName + lastName, description, email, order);
        Optional<User> optionalUser = Optional.of(expectedUser);
        when(userDao.getById(userId)).thenReturn(optionalUser);
        assertEquals(expectedUser, userService.findById(userId));
        verify(userDao).getById(userId);
        verifyNoMoreInteractions(userDao);
    }

    @ParameterizedTest
    @DisplayName("Given an ID and an order, when getUserByIdWithOrder method is called and a user is found, then the user entity with the specified order is returned")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void testGetUserByIdWithOrder(Long userId, String firstName, String lastName, String email,
                                  long certificateId, String name, String description,
                                  BigDecimal price, int duration) {
        Certificate certificate = Certificate.builder().id(certificateId).name(name).description(description).duration(duration).build();
        Order order = Order.builder().id(certificateId).cost(price).certificates(Collections.singleton(certificate)).build();
        User expectedUser = getUser(userId, firstName + lastName, description, email, order);
        when(userDao.getById(userId)).thenReturn(Optional.of(expectedUser));
        when(mapper.toDto(expectedUser)).thenReturn(UserDto.builder().id(userId).username(firstName + lastName).email(email).build());
        UserDto actualUserDto = userService.getById(userId);
        assertEquals(expectedUser.getId(), actualUserDto.getId());
        assertEquals(expectedUser.getEmail(), actualUserDto.getEmail());
        verify(userDao).getById(userId);
        verify(mapper).toDto(expectedUser);
        verifyNoMoreInteractions(userDao, mapper);
    }

    @ParameterizedTest
    @DisplayName("Given a user entity, when update method is called, then the user entity is updated in the database")
    @CsvSource({
            "1, Olivia, password1, olivia@example.com, GUEST",
            "2, Emma, password2, emma@example.com, USER",
            "3, Charlotte, password3, charlotte@example.com, ADMIN"
    })
    void testUpdateUser(Long userId, String password, String username, String email, String role) {
        UserSlimDto slimDto = getUserSlimDto(userId, password, username, email, role);
        User user = getUser(userId, password, username, email, role);
        UserDto expectedUserDto = getUserDto(userId, username, email);
        when(mapper.toEntity(slimDto)).thenReturn(user);
        when(userDao.update(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(expectedUserDto);
        UserDto updatedUserDto = userService.update(slimDto);
        verify(mapper).toEntity(slimDto);
        verify(userDao).update(user);
        verify(mapper).toDto(user);
        verify(userDao, times(1)).update(any(User.class));
        verify(mapper, times(1)).toEntity(any(UserSlimDto.class));
        verify(mapper, times(1)).toDto(any(User.class));
        assertNotNull(updatedUserDto);
        assertEquals(expectedUserDto.getId(), updatedUserDto.getId());
        assertEquals(expectedUserDto.getUsername(), updatedUserDto.getUsername());
        assertEquals(expectedUserDto.getEmail(), updatedUserDto.getEmail());
    }

    @Test
    @DisplayName("Given an ID, when delete method is called, then the user with the given ID is deleted from the database")
    void testDeleteUser() {
        doNothing().when(userDao).delete(anyLong());
        assertDoesNotThrow(() -> userService.delete(id));
        verify(userDao, times(1)).delete(anyLong());
    }

    private static User getUser(Long userId, String password, String username, String email, String role) {
        return User.builder().id(userId).username(username).password(password).email(email)
                .role(Role.builder().permission(RoleType.valueOf(role)).build()).build();
    }

    private static User getUser(Long userId, String firstName, String lastName, String email, Order order) {
        return User.builder().id(userId).username(firstName + "-" + lastName).orders(Collections.singleton(order)).email(email).build();
    }

    private static UserSlimDto getUserSlimDto(Long userId, String password, String username, String email, String role) {
        return UserSlimDto.builder()
                .id(userId)
                .username(username)
                .password(password)
                .email(email)
                .role(RoleDto.builder().permission(RoleType.valueOf(role)).build())
                .build();
    }

    private static UserDto getUserDto(Long userId, String username, String email) {
        return UserDto.builder()
                .id(userId)
                .username(username)
                .email(email)
                .build();
    }
}
