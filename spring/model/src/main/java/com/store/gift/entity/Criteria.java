package com.store.gift.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the criteria for searching certificates.
 */
@Getter
@Setter
@Builder
public class Criteria {
    /**
     * The name of the certificate.
     */
    private String name;

    /**
     * The description of the certificate.
     */
    private String description;

    /**
     * The list of tag names associated with the certificate.
     */
    private List<String> tagNames;
}
