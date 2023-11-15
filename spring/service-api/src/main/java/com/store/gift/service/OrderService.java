package com.store.gift.service;

import com.store.gift.dto.OrderDto;
import com.store.gift.entity.Certificate;
import com.store.gift.entity.Tag;
import com.store.gift.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for managing orders.
 */
public interface OrderService {

    /**
     * Get a specific order of a user.
     *
     * @param userId  the user
     * @param orderId the order ID
     * @return the order DTO
     */
    OrderDto getUserOrder(Long userId, Long orderId);

    /**
     * Get the most used tags by a user.
     *
     * @param userId the user ID
     * @return the most used tag
     */
    Optional<Tag> getMostUsedTags(Long userId);

    /**
     * Get all orders of a user.
     *
     * @param userId   the user ID
     * @param pageable the number of the page
     * @return the page of order DTOs
     */
    List<OrderDto> getAllByUserId(Long userId, Pageable pageable);

    /**
     * Create an order for a user with the specified certificate IDs.
     *
     * @param userId         the user ID
     * @param certificateIds the set of certificate IDs
     * @return the created order DTO
     */
    OrderDto createOrder(Long userId, Set<Long> certificateIds);

    /**
     * Get paginated user orders.
     *
     * @param user     the user
     * @param pageable the pageable information
     * @return the page of order DTOs
     */
    Page<OrderDto> getUserOrders(User user, Pageable pageable);

    /**
     * Get all orders with pagination.
     *
     * @param pageable the pageable information
     * @return the page of order DTOs
     */
    Page<OrderDto> getAll(Pageable pageable);

    /**
     * Find a certificate by ID.
     *
     * @param ids the certificate IDs
     * @return the certificate
     */
    List<Certificate> findCertificateById(Set<Long> ids);

    /**
     * Save an order.
     *
     * @param username the user username
     * @param ids      the certificate IDs
     * @param counters Set<Long> counters
     * @return the saved order DTO
     */
    OrderDto save(String username, Set<Long> ids, List<Long> counters);

    /**
     * Get an order by ID.
     *
     * @param id the order ID
     * @return the order DTO
     */
    OrderDto getById(Long id);

    OrderDto update(OrderDto dto);

    void delete(Long id);

}
