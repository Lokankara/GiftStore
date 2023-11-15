package com.store.gift.service;

import com.store.gift.dao.CertificateDao;
import com.store.gift.dao.OrderDao;
import com.store.gift.dao.UserDao;
import com.store.gift.dto.CertificateDto;
import com.store.gift.dto.OrderDto;
import com.store.gift.dto.UserSlimDto;
import com.store.gift.entity.Certificate;
import com.store.gift.entity.Invoice;
import com.store.gift.entity.Order;
import com.store.gift.entity.Tag;
import com.store.gift.entity.User;
import com.store.gift.exception.CertificateNotFoundException;
import com.store.gift.exception.OrderNotFoundException;
import com.store.gift.exception.TagNotFoundException;
import com.store.gift.exception.UserNotFoundException;
import com.store.gift.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private UserDao userDao;
    @Mock
    private CertificateDao certificateDao;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderService;
    private final Long orderId = 1L;
    private final Long userId = 1L;
    Set<Long> certificateIds = new HashSet<>();
    private final Long id = 1L;
    private final Long id2 = 2L;
    private final User user = User.builder()
            .id(userId)
            .username("Olivia-Noah")
            .email("Olivia-Noah@gmail.com")
            .build();
    private final User user2 = User.builder()
            .id(2L)
            .username("Emma-Liam")
            .email("Emma-Liam@gmail.com")
            .build();

    private final UserSlimDto userDto2 = UserSlimDto.builder()
            .id(2L)
            .username("Emma-Liam")
            .email("Emma-Liam@gmail.com")
            .build();
    private final UserSlimDto userDto = UserSlimDto.builder()
            .id(userId)
            .username("Olivia-Noah")
            .email("Olivia-Noah@gmail.com")
            .build();
    private final Certificate certificate = Certificate.builder()
            .id(id)
            .name("Java")
            .description("Core")
            .price(BigDecimal.valueOf(100))
            .duration(50)
            .build();
    private final CertificateDto certificateDto = CertificateDto.builder()
            .id(id)
            .name("Java")
            .description("Core")
            .price(BigDecimal.valueOf(100))
            .duration(50)
            .build();
    private final Certificate certificate2 = Certificate.builder()
            .id(id2)
            .name("Spring")
            .description("Boot")
            .price(BigDecimal.valueOf(20))
            .duration(45)
            .build();
    private final CertificateDto certificateDto2 = CertificateDto.builder()
            .id(id2)
            .name("Spring")
            .description("Boot")
            .price(BigDecimal.valueOf(20))
            .duration(45)
            .build();

    private final Order order = Order.builder().id(1L).user(user)
            .certificates(Collections.singleton(certificate)).build();
    private final Order order2 = Order.builder().id(2L).user(user2)
            .certificates(Collections.singleton(certificate2)).build();
    private final OrderDto orderDto = OrderDto.builder().id(id).user(userDto)
            .certificateDtos(Collections.singleton(certificateDto)).build();
    private final OrderDto expectedOrderDto = OrderDto.builder().id(id2).user(userDto2)
            .certificateDtos(Collections.singleton(certificateDto2)).build();
    private final List<OrderDto> orderDtos = Arrays.asList(orderDto, expectedOrderDto);
    private final List<Order> orders = Arrays.asList(order, order2);
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());

    @BeforeEach
    public void setUp() {
        certificateIds.add(10L);
        certificateIds.add(20L);
        certificateIds.add(30L);
    }

    @Test
    @DisplayName("Call the save method and verify that it throws a UserNotFoundException return an empty Optional")
    void testSaveThrowsUserNotFoundException() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));
        assertThrows(UserNotFoundException.class, () -> orderService.save(user.getUsername(), ids, new ArrayList<>(ids)));
    }

    @Test
    @DisplayName("Get Order by ID")
    void testGetById() {
        when(orderDao.getById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto actualOrderDto = orderService.getById(orderId);
        assertEquals(orderDto.getId(), actualOrderDto.getId());
        assertEquals(orderDto.getUser().getEmail(), actualOrderDto.getUser().getEmail());
        assertEquals(orderDto.getUser().getUsername(), actualOrderDto.getUser().getUsername());
        assertEquals(orderDto.getCertificateDtos().size(), actualOrderDto.getCertificateDtos().size());
        assertEquals(orderDto.getCertificateDtos().iterator().next().getId(),
                actualOrderDto.getCertificateDtos().iterator().next().getId());
        verify(orderDao).getById(orderId);
        verify(orderMapper).toDto(order);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Get Order by ID - Order Not Found")
    void getByIdOrderNotFoundTest() {
        when(orderDao.getById(orderId)).thenReturn(Optional.empty());
        Executable executable = () -> orderService.getById(orderId);
        assertThrows(OrderNotFoundException.class, executable);
        verify(orderDao).getById(orderId);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }


    @Test
    @DisplayName("Create Order")
    void createOrderTest() {
        Set<Long> certificateIds = Collections.singleton(id);
        List<Certificate> certificates = Collections.singletonList(certificate);
        when(userDao.getById(userId)).thenReturn(Optional.of(user));
        when(certificateDao.findAllByIds(certificateIds)).thenReturn(certificates);
        when(orderDao.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto actualOrderDto = orderService.createOrder(userId, certificateIds);
        assertEquals(orderDto.getId(), actualOrderDto.getId());
        assertEquals(orderDto.getUser().getEmail(), actualOrderDto.getUser().getEmail());
        assertEquals(orderDto.getUser().getUsername(), actualOrderDto.getUser().getUsername());
        assertEquals(orderDto.getCertificateDtos().size(), actualOrderDto.getCertificateDtos().size());
        assertEquals(orderDto.getCertificateDtos().iterator().next().getId(),
                actualOrderDto.getCertificateDtos().iterator().next().getId());
        verify(userDao).getById(userId);
        verify(certificateDao).findAllByIds(certificateIds);
        verify(orderDao).save(any(Order.class));
        verify(orderMapper).toDto(order);
        verifyNoMoreInteractions(userDao, certificateDao, orderDao, orderMapper);
    }

    @Test
    @DisplayName("Save Order - UserNotFoundException")
    void saveOrderUserNotFoundException() {
        when(userDao.getById(userId)).thenReturn(Optional.empty());
        Executable executable = () -> orderService.createOrder(userId, certificateIds);
        assertThrows(UserNotFoundException.class, executable);
        verify(userDao).getById(userId);
        verifyNoMoreInteractions(userDao, certificateDao, orderDao, orderMapper);
    }

    @Test
    @DisplayName("Save Order - CertificateNotFoundException")
    void saveOrderCertificateNotFoundExceptionTest() {
        when(userDao.getById(userId)).thenReturn(Optional.of(user));
        when(certificateDao.findAllByIds(certificateIds)).thenReturn(Collections.emptyList());
        Executable executable = () -> orderService.createOrder(userId, certificateIds);
        assertThrows(CertificateNotFoundException.class, executable);
        verify(userDao).getById(userId);
        verify(certificateDao).findAllByIds(certificateIds);
        verifyNoMoreInteractions(userDao, certificateDao, orderDao, orderMapper);
    }

    @ParameterizedTest
    @DisplayName("Get User Orders")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void getUserOrdersTest(Long orderId, String firstName, String lastName, String email,
                           long certificateId, String name, String description, BigDecimal price, int duration) {
        List<Order> expectedOrders = Collections.singletonList(order);
        List<OrderDto> expectedOrderDtos = Collections.singletonList(OrderDto.builder()
                .id(orderId)
                .user(UserSlimDto.builder()
                        .username(firstName + "-" + lastName)
                        .email(email)
                        .build())
                .certificateDtos(Collections.singleton(CertificateDto.builder()
                        .id(certificateId)
                        .name(name)
                        .description(description)
                        .price(price)
                        .duration(duration)
                        .build()))
                .build());
        when(orderDao.getUserOrders(user, pageable)).thenReturn(expectedOrders);
        when(orderMapper.toDtoList(expectedOrders)).thenReturn(expectedOrderDtos);
        Page<OrderDto> actualOrderDtos = orderService.getUserOrders(user, pageable);
        assertEquals(expectedOrderDtos.size(), actualOrderDtos.getContent().size());
        assertEquals(expectedOrderDtos.get(0).getId(), actualOrderDtos.getContent().get(0).getId());
        assertEquals(expectedOrderDtos.get(0).getUser().getEmail(),
                actualOrderDtos.getContent().get(0).getUser().getEmail());
        verify(orderDao).getUserOrders(user, pageable);
        verify(orderMapper).toDtoList(expectedOrders);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Get User Order - OrderNotFoundException")
    void getUserOrderOrderNotFoundExceptionTest() {
        when(orderDao.getUserOrder(user.getId(), orderId)).thenReturn(Optional.empty());
        Executable executable = () -> orderService.getUserOrder(user.getId(), orderId);
        assertThrows(OrderNotFoundException.class, executable);
        verify(orderDao).getUserOrder(user.getId(), orderId);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Test get user order")
    void getUserOrderTest() {
        when(orderDao.getUserOrder(user.getId(), orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto actualOrderDto = orderService.getUserOrder(user.getId(), orderId);
        assertEquals(orderDto, actualOrderDto);
        verify(orderDao).getUserOrder(user.getId(), orderId);
        verify(orderMapper).toDto(order);
    }

    @Test
    @DisplayName("Test get user order throws order not found exception")
    void getUserOrderThrowsOrderNotFoundExceptionTest() {
        when(orderDao.getUserOrder(user.getId(), orderId)).thenReturn(Optional.empty());
        Executable getUserOrderExecution = () -> orderService.getUserOrder(user.getId(), orderId);
        assertThrows(OrderNotFoundException.class, getUserOrderExecution);
        verify(orderDao).getUserOrder(user.getId(), orderId);
    }

    @ParameterizedTest
    @DisplayName("Get User Orders")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void getAll(Long id, String firstName, String lastName, String email, long certificateId,
                String name, String description, BigDecimal price, int duration) {

        List<Order> expectedOrders = Collections.singletonList(order);
        List<OrderDto> expectedOrderDtos = Collections.singletonList(OrderDto.builder()
                .id(id)
                .user(UserSlimDto.builder()
                        .username(firstName + "-" + lastName)
                        .email(email)
                        .build())
                .certificateDtos(Collections.singleton(CertificateDto.builder()
                        .id(certificateId)
                        .name(name)
                        .description(description)
                        .price(price)
                        .duration(duration)
                        .build()))
                .build());

        when(orderDao.getAllBy(pageable)).thenReturn(expectedOrders);
        when(orderMapper.toDtoList(expectedOrders)).thenReturn(expectedOrderDtos);
        Page<OrderDto> result = orderService.getAll(pageable);
        assertNotNull(result);
        assertEquals(expectedOrderDtos, result.getContent());
    }

    @ParameterizedTest
    @DisplayName("Get User Orders")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void getAllByUserIdTest(Long id, String firstName, String lastName, String email, long certificateId,
                            String name, String description, BigDecimal price, int duration) {

        List<Order> expectedOrders = Collections.singletonList(order);
        List<OrderDto> expectedOrderDtos = Collections.singletonList(OrderDto.builder()
                .id(id)
                .user(UserSlimDto.builder()
                        .username(firstName + "-" + lastName)
                        .email(email)
                        .build())
                .certificateDtos(Collections.singleton(CertificateDto.builder()
                        .id(certificateId)
                        .name(name)
                        .description(description)
                        .price(price)
                        .duration(duration)
                        .build()))
                .build());

        when(orderDao.findOrdersByUserId(id, pageable)).thenReturn(expectedOrders);
        when(orderMapper.toDtoList(expectedOrders)).thenReturn(expectedOrderDtos);
        List<OrderDto> actualOrderDtos = orderService.getAllByUserId(id, pageable);
        assertEquals(expectedOrderDtos, actualOrderDtos);
        verify(orderDao).findOrdersByUserId(id, pageable);
        verify(orderMapper).toDtoList(expectedOrders);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2, Java, description, 10, 30",
            "2, 3, 7, Oliver, description, 20, 45",
            "3, 6, 3, Spring, description, 30, 60",
            "4, 7, 6, SQL, description, 40, 75",
            "5, 8, 4, Programming, description, 50, 90"
    })
    @DisplayName("testFindCertificateById")
    void testFindCertificateById(long id1, long id2, long certificateId, String name,
                                 String description, BigDecimal price, int duration) {
        Set<Long> ids = new HashSet<>(Arrays.asList(id1, id2));

        Certificate certificate = Certificate.builder()
                .id(certificateId)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();

        List<Certificate> expectedCertificates = new ArrayList<>();
        expectedCertificates.add(certificate);
        when(certificateDao.findAllByIds(ids)).thenReturn(expectedCertificates);
        List<Certificate> result = orderService.findCertificateById(ids);
        assertEquals(expectedCertificates, result);
        verify(certificateDao).findAllByIds(ids);
    }

    @Test
    @DisplayName("Get All Orders")
    void getAllOrdersTest() {
        when(orderDao.getAllBy(pageable)).thenReturn(orders);
        when(orderMapper.toDtoList(orders)).thenReturn(orderDtos);
        Page<OrderDto> result = orderService.getAll(pageable);
        assertNotNull(result);
        assertEquals(orders.size(), result.getContent().size());

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            OrderDto orderDto = result.getContent().get(i);
            assertEquals(order.getId(), orderDto.getId());
            assertEquals(order.getUser().getUsername(), orderDto.getUser().getUsername());
            assertEquals(order.getUser().getEmail(), orderDto.getUser().getEmail());
            Set<Certificate> certificates = order.getCertificates();
            Set<CertificateDto> certificateDtos = orderDto.getCertificateDtos();
            assertEquals(certificates.size(), certificateDtos.size());
            Iterator<Certificate> certificateIterator = certificates.iterator();
            Iterator<CertificateDto> certificateDtoIterator = certificateDtos.iterator();
            while (certificateIterator.hasNext() && certificateDtoIterator.hasNext()) {
                Certificate certificate = certificateIterator.next();
                CertificateDto certificateDto = certificateDtoIterator.next();
                assertEquals(certificate.getId(), certificateDto.getId());
                assertEquals(certificate.getName(), certificateDto.getName());
                assertEquals(certificate.getDescription(), certificateDto.getDescription());
                assertEquals(certificate.getPrice(), certificateDto.getPrice());
                assertEquals(certificate.getDuration(), certificateDto.getDuration());
            }
        }
        verify(orderDao).getAllBy(pageable);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Get Most Used Tags")
    void getMostUsedTagsTest() {
        Tag tag = Tag.builder()
                .id(id)
                .name("Spring")
                .build();
        when(orderDao.getMostUsedTagBy(userId)).thenReturn(Optional.of(tag));
        Tag result = orderService.getMostUsedTags(userId)
                .orElseThrow(() -> new TagNotFoundException(
                        "Tag not found Exception"));
        assertNotNull(result);
        assertEquals(tag.getId(), result.getId());
        assertEquals(tag.getName(), result.getName());
        verify(orderDao).getMostUsedTagBy(userId);
        verifyNoMoreInteractions(orderDao);
    }

    @Test
    void saveOrderTest() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));
        Certificate certificate1 = new Certificate();
        certificate1.setId(1L);
        certificate1.setPrice(new BigDecimal("50.0"));
        List<Certificate> certificates = Arrays.asList(certificate1, certificate2);

        BigDecimal totalPrice = BigDecimal.valueOf(90);

        Order order = Order.builder()
                .certificates(new HashSet<>(certificates))
                .cost(totalPrice)
                .user(user)
                .build();

        Invoice invoice = new Invoice();
        order.setInvoices(Collections.singletonList(invoice));

        Order savedOrder = Order.builder()
                .id(1L)
                .orderDate(order.getOrderDate())
                .certificates(order.getCertificates())
                .cost(order.getCost())
                .user(order.getUser())
                .invoices(Collections.singletonList(invoice))
                .build();

        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(certificateDao.findAllByIds(ids)).thenReturn(certificates);
        doReturn(savedOrder).when(orderDao).save(any(Order.class));
        when(orderMapper.toDto(savedOrder)).thenReturn(expectedOrderDto);
        OrderDto result = orderService.save(user.getUsername(), ids, new ArrayList<>(ids));
        assertEquals(expectedOrderDto, result);
        verify(orderDao).save(any(Order.class));
        assertNotNull(result);
    }

    @Test
    void testUpdate() {
        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(orderDao.update(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expectedOrderDto);
        OrderDto actualOrderDto = orderService.update(orderDto);
        assertNotNull(actualOrderDto);
    }

    @Test
    void testDelete() {
        doNothing().when(orderDao).delete(id);
        orderService.delete(id);
        verify(orderDao).delete(id);
    }
}
