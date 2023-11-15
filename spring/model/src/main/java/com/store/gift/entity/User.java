package com.store.gift.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User class representing a user in the system.
 * <p>
 * Implements UserDetails interface for Spring Security integration.
 */
@Slf4j
@Getter
@Setter
@Entity
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@NamedEntityGraph(
        name = "User.orders.certificates.tags.role",
        attributeNodes = {
                @NamedAttributeNode(value = "orders", subgraph = "orderGraph"),
                @NamedAttributeNode(value = "role", subgraph = "roleGraph"),
                @NamedAttributeNode("tokens")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "orderGraph",
                        attributeNodes = {
                                @NamedAttributeNode(
                                        value = "certificates",
                                        subgraph = "certificateGraph"
                                )
                        }
                ),
                @NamedSubgraph(
                        name = "certificateGraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "tags")
                        }
                ),
                @NamedSubgraph(
                        name = "roleGraph",
                        attributeNodes = {
                                @NamedAttributeNode("authorities")
                        }
                )
        }
)
public class User implements Serializable {
    /**
     * The unique identifier of the user.
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence",
            sequenceName = "user_id_seq",
            allocationSize = 1)
    private Long id;

    /**
     * The username of the user.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * The email address of the user.
     */
    @Column(name = "email", nullable = false)
    @Email(message = "{invalid.email}")
    private String email;

    /**
     * The password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The set of orders associated with the user.
     */
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    /**
     * The set of tokens associated with the user.
     */
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> tokens = new HashSet<>();

    /**
     * The role of a user.
     */
    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "role_id")
    private Role role;
}
