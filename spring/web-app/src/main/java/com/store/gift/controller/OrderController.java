package com.store.gift.controller;

import com.store.gift.assembler.OrderAssembler;
import com.store.gift.dto.OrderDto;
import com.store.gift.entity.Tag;
import com.store.gift.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Controller class for handling order-related operations.
 * <p>
 * This class is annotated with {@link RestController} to indicate that it is a Spring MVC controller,
 * and {@link RequestMapping} with a value of "/orders" to map requests to this controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@CrossOrigin(origins = {
        "http://192.168.31.177:4200",
        "http://localhost:5500",
        "http://localhost:4200",
        "http://127.0.0.1:5500",
        "http://127.0.0.1:8080",
        "http://127.0.0.1:4200",
        "https://gift-store-angular.netlify.app",
        "https://gift-store-certificate.netlify.app",
        "https://gift-store.onrender.com"})
public class OrderController {
    /**
     * The tag service for performing tag-related operations.
     */
    private final OrderService orderService;

    /**
     * The Order assembler for converting tag entities to DTOs.
     */
    private final OrderAssembler assembler;

    /**
     * Creates a new order for the specified user and certificate.
     *
     * @param username         the ID of the user
     * @param certificateIds the ID of the certificate
     * @return the created order DTO
     */
    @PostMapping("/{username}")
    @ResponseStatus(CREATED)
    public EntityModel<OrderDto> create(
            @PathVariable final String username,
            @RequestParam final Set<Long> certificateIds,
            @RequestParam final List<Long> counters

            ) {
        return assembler.toModel(
                orderService.save(username, certificateIds, counters));
    }

    /**
     * Retrieves all orders for a specific user by user ID.
     *
     * @param userId   the ID of the user.
     * @param pageable the pagination information.
     * @return a collection of order resources.
     */
    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<OrderDto>> getAllOrdersByUserId(
            @PathVariable final Long userId,
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                orderService.getAllByUserId(userId, pageable));
    }

    /**
     * Retrieves all orders with pagination.
     *
     * @param pageable the pagination information.
     * @return a collection of order resources.
     */
    @GetMapping
    public CollectionModel<EntityModel<OrderDto>> getAllOrders(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                orderService.getAll(pageable));
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order.
     * @return the order resource.
     */
    @GetMapping("/{id}")
    public EntityModel<OrderDto> getOrderById(
            @PathVariable final Long id) {
        return assembler.toModel(
                orderService.getById(id));
    }

    /**
     * Retrieves the most used tag for a specific user.
     *
     * @param userId the ID of the user
     * @return the ResponseEntity with the most used tag if found,
     * or a not found response if not found
     */
    @GetMapping("/users/{userId}/most")
    public ResponseEntity<Tag> getMostUsedTag(
            final @PathVariable Long userId) {
        Optional<Tag> mostUsedTag =
                orderService.getMostUsedTags(userId);
        return mostUsedTag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves the details of an order for a specific user.
     *
     * @param orderId the ID of the order
     * @param userId  the ID of the user
     * @return the EntityModel containing the order details
     */
    @GetMapping("/{orderId}/users/{userId}")
    public EntityModel<OrderDto> getOrderDetails(
            final @PathVariable Long orderId,
            final @PathVariable Long userId) {
        return assembler.toModel(
                orderService.getUserOrder(orderId, userId));
    }

    /**
     * Updates an existing order with the given ID using the provided order DTO.
     *
     * @param id  the ID of the order to be updated
     * @param dto the updated order DTO
     * @return the updated order as an EntityModel
     */
    @PatchMapping(value = "/{id}")
    public EntityModel<OrderDto> update(
            @Valid @PathVariable final Long id,
            @Valid @RequestBody final OrderDto dto) {
        dto.setId(id);
        return assembler.toModel(
                orderService.update(dto));
    }

    /**
     * Deletes the order with the given ID.
     *
     * @param id the ID of the order to be deleted
     * @return a ResponseEntity with HTTP status NO_CONTENT
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable final Long id) {
        orderService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
