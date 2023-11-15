package com.store.gift.dao;

/**
 * Contains constant strings for SQL queries.
 */
public final class Queries {
    private Queries() {
    }

    /**
     * Constant representing the "certificates" resource.
     */
    public static final String CERTIFICATES = "certificates";

    /**
     * Constant representing the "orders" resource.
     */
    public static final String ORDERS = "orders";

    /**
     * Constant representing the "id" field.
     */
    public static final String ID = "id";

    /**
     * Constant representing the "tags" field.
     */
    public static final String TAGS = "tags";

    /**
     * Constant representing the "name" field.
     */
    public static final String NAME = "name";

    /**
     * Constant representing the "description" field.
     */
    public static final String DESCRIPTION = "description";

    /**
     * Constant representing the "user" field.
     */
    public static final String USER = "user";
    /**
     * Constant for the fetch graph hint used in entity manager queries.
     */
    public static final String FETCH_GRAPH = "jakarta.persistence.fetchgraph";
    /**
     * SQL query to select all certificates.
     */
    public static final String SELECT_ALL = "SELECT c FROM Certificate c";

    /**
     * SQL query to select a certificate by name.
     */
    public static final String SELECT_BY_NAME = SELECT_ALL
            + " WHERE c.name = :name";

    /**
     * SQL query to select certificates by user ID.
     */
    public static final String SELECT_CERTIFICATES_BY_USER_ID = SELECT_ALL
            + " JOIN c.orders o JOIN o.user u WHERE u.id = :id";

    /**
     * SQL query to select certificates by a list of IDs.
     */
    public static final String SELECT_ALL_BY_IDS = SELECT_ALL + " WHERE c.id IN :ids";

    /**
     * SQL query to select certificates by order ID.
     */
    public static final String SELECT_CERTIFICATES_BY_ORDER_ID = "SELECT DISTINCT c "
            + "FROM Certificate c "
            + "JOIN FETCH c.orders o "
            + "JOIN FETCH o.user "
            + "WHERE o.id = :orderId";

    /**
     * SQL query to select tags by name.
     */
    public static final String SELECT_TAGS_BY_NAME = "SELECT t FROM Tag t WHERE t.name = :name";

    /**
     * SQL query to select tags by certificate ID.
     */
    public static final String SELECT_TAGS_BY_ID = "SELECT t FROM Tag t JOIN t.certificates c WHERE c.id = :id";

    /**
     * SQL query to select an order by username.
     */
    public static final String SELECT_ORDER_BY_NAME = "SELECT o FROM Order o WHERE o.user.username = :username";
    public static final String SELECT_ORDER_BY_ID = "SELECT DISTINCT o FROM Order o "
            + "LEFT JOIN FETCH o.certificates c "
            + "LEFT JOIN FETCH c.tags "
            + "LEFT JOIN FETCH o.user "
            + "WHERE o.id = :id";

    /**
     * SQL query to select a user by name.
     */
    public static final String SELECT_USER_BY_NAME = "SELECT u FROM User u WHERE u.username = :name";
    /**
     * SQL query to select tags by their names.
     */
    public static final String SELECT_TAG_BY_NAMES = "SELECT t FROM Tag t WHERE t.name IN :names";
    /**
     * SQL query to select orders by their certificate ids.
     */
    public static final String SELECT_ORDER_BY_IDS = "SELECT o FROM Order o WHERE o.id IN :orderIds";
    /**
     * SQL query to delete an order certificate by certificate id.
     */
    public static final String DELETE_ORDER_CERTIFICATE = "DELETE FROM order_certificate WHERE certificate_id = :id";
    /**
     * SQL query to delete a certificate tag by gift certificate id.
     */
    public static final String DELETE_CERTIFICATE_TAG = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = :id";
    /**
     * SQL query to delete a certificate tag by tag id.
     */
    public static final String DELETE_CT_BY_TAG_ID = "DELETE FROM gift_certificate_tag WHERE tag_id = :id";
    /**
     * SQL query to delete a gift certificate by its id.
     */
    public static final String DELETE_CERTIFICATE = "DELETE FROM gift_certificates WHERE id = :id";
    /**
     * SQL query to delete a tag by its id.
     */
    public static final String DELETE_TAG = "DELETE FROM Tag t WHERE t.id = :id";
    /**
     * SQL query for deleting orders by user ID.
     */
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE user_id = :id";
    /**
     * SQL query for deleting tokens by user ID.
     */
    public static final String DELETE_TOKEN = "DELETE FROM tokens WHERE user_id = :id";
    /**
     * SQL query for deleting users by user ID.
     */
    public static final String DELETE_USER = "DELETE FROM users WHERE user_id = :id";
}
