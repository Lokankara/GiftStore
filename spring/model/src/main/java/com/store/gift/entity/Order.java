package com.store.gift.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an order entity.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditListener.class)
@NamedEntityGraph(
        name = "Order.certificates.tags",
        attributeNodes = {
                @NamedAttributeNode(
                        value = "certificates",
                        subgraph = "certificateGraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "certificateGraph",
                        attributeNodes = {
                                @NamedAttributeNode("tags")
                        }
                )
        }
)
public class Order implements Serializable {
    /**
     * The unique identifier of the order.
     */
    @Id
    @Column(name = "order_id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence")
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_id_seq",
            allocationSize = 1)
    private Long id;

    /**
     * The date of the order.
     */
    @CreationTimestamp
    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;

    /**
     * The cost of the order.
     */
    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    /**
     * The user who placed the order.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The set of certificates associated with the order.
     */
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "order_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id")
    )
    private Set<Certificate> certificates = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    /**
     * Adds a certificate to the order.
     *
     * @param certificate the certificate to add.
     * @return the updated order.
     */
    public Order addCertificate(
            final Certificate certificate) {
        if (certificate != null) {
            certificates.add(certificate);
            certificate.getOrders().add(this);
        }
        return this;
    }

    /**
     * Removes a certificate from the order.
     *
     * @param certificate the certificate to remove.
     * @return the updated order.
     */
    public Order removeCertificate(
            final Certificate certificate) {
        if (certificate != null
                && certificates.contains(certificate)) {
            certificates.remove(certificate);
            certificate.getOrders().remove(this);
        }
        return this;
    }
}
