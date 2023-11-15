package com.store.gift.dao;

import com.store.gift.entity.User;

/**
 * Data access interface for managing users.
 */
public interface UserDao extends Dao<User> {
    /**
     * Updates the specified User.
     *
     * @param user the User to update
     * @return the updated User
     */
    User update(User user);
}
