package com.store.gift.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a tag entity.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag")
public class Tag implements Serializable {
    /**
     * The unique identifier of the tag.
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence")
    @SequenceGenerator(name = "tag_sequence",
            sequenceName = "tag_id_seq",
            allocationSize = 1)
    private Long id;

    /**
     * The name of the tag.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * The set of certificates associated with the tag.
     */
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private Set<Certificate> certificates = new HashSet<>();

    /**
     * Adds a certificate to the tag.
     *
     * @param certificate the certificate to add
     * @return the updated tag instance
     */
    public Tag addCertificate(
            final Certificate certificate) {
        if (certificate != null) {
            if (certificates == null) {
                this.certificates = new HashSet<>();
            }
            certificates.add(certificate);
            certificate.getTags().add(this);
        }
        return this;
    }

    /**
     * Removes a certificate from the tag.
     *
     * @param certificate the certificate to remove
     * @return the updated tag instance
     */
    public Tag removeCertificate(
            final Certificate certificate) {
        if (certificate != null && certificates.contains(certificate)) {
            this.certificates.remove(certificate);
            certificate.getTags().remove(this);
        }
        return this;
    }
}
