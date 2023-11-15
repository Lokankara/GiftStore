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
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a gift certificate.
 *
 * <p>The {@code Certificate} class is an entity class mapped
 * to the "gift_certificates" table in the database.
 * <p>Tags and orders are represented as many-to-many relationships
 * with the {@link Tag} and {@link Order} entities.
 * A certificate can have multiple tags,
 * and it can be associated with multiple orders
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_certificates")
@EntityListeners(AuditListener.class)
@NamedEntityGraph(name = "Certificate.tags",
        attributeNodes = @NamedAttributeNode("tags"))
public class Certificate implements Serializable {
    /**
     * The unique identifier of the certificate.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "certificate_sequence")
    @SequenceGenerator(name = "certificate_sequence",
            sequenceName = "gift_certificate_id_seq",
            allocationSize = 1)
    private Long id;
    /**
     * The name of the certificate.
     */
    @Column(nullable = false, length = 512)
    private String name;

    /**
     * The price of the certificate.
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * The description of the certificate.
     */
    @Column(nullable = false, length = 1024)
    private String description;
    private String shortDescription;
    private String company;

    /**
     * The creation date of the certificate.
     */
    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    /**
     * The last update date of the certificate.
     */
    @UpdateTimestamp
    @Column(name = "last_update_date", nullable = false)
    private Timestamp lastUpdateDate;

    /**
     * The duration of the certificate.
     */
    @Column(nullable = false)
    private Integer duration;

    @Column(length = 1024)
    private String path;

    /**
     * The set of tags associated with the certificate.
     * <p>
     * This is a many-to-many relationship mapped
     * by the "gift_certificate_tag" join table.
     * <p>
     * The tags are eagerly fetched
     * and cascaded on persist and merge operations.
     */
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    /**
     * The set of orders associated with the certificate.
     * <p>
     * This is a many-to-many relationship mapped
     * by the "certificates" field in the Order entity.
     * <p>
     * The orders are eagerly fetched
     * and cascaded on persist and merge operations.
     */
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(
            mappedBy = "certificates",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER)
    private Set<Order> orders = new HashSet<>();

    /**
     * Adds a tag to the certificate.
     *
     * @param tag the tag to be added
     * @return the updated certificate
     */
    public Certificate addTag(final Tag tag) {
        if (tag != null) {
            if (tags == null) {
                this.tags = new HashSet<>();
            }
            this.tags.add(tag);
        }
        return this;
    }

    /**
     * Removes a tag from the certificate.
     *
     * @param tag the tag to be removed
     * @return the updated certificate
     */
    public Certificate removeTag(final Tag tag) {
        if (tags != null) {
            this.tags.remove(tag);
        }
        return this;
    }

    /**
     * Adds an order to the certificate.
     *
     * @param order the order to be added
     * @return the updated certificate
     */
    public Certificate addOrder(final Order order) {
        if (order != null) {
            if (this.orders == null) {
                this.orders = new HashSet<>();
            }
            this.orders.add(order);
            order.getCertificates().add(this);
        }
        return this;
    }

    /**
     * Removes an order from the certificate.
     *
     * @param order the order to be removed
     * @return the updated certificate
     */
    public Certificate removeOrder(
            final Order order) {
        if (order != null && this.orders != null) {
            this.orders.remove(order);
            order.getCertificates().remove(this);
        }
        return this;
    }
}
