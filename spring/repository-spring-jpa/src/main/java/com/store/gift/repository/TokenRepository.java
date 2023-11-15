package com.store.gift.repository;

import com.store.gift.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The TokenRepository interface is responsible for defining the operations to
 * <p>
 * access and manipulate tokens in the database.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    /**
     * Retrieves a token by its access token.
     *
     * @param token the access token
     * @return an optional containing the token, or an empty optional if not found
     */
    Optional<Token> findByAccessToken(String token);

    /**
     * Retrieves all valid access tokens associated with a user.
     *
     * @param id the ID of the user
     * @return a list of valid tokens
     */
    @Query(value = "select t from Token t inner join User u on t.user.id = u.id where u.id = :id and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidAccessTokenByUserId(Long id);
}
