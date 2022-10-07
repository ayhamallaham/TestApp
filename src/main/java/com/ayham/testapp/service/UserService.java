package com.ayham.testapp.service;

import com.ayham.testapp.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link User}.
 */
public interface UserService {
    /**
     * Save a myUser.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    User save(User user);

    /**
     * Registers a user
     * @param user to register
     */
    User register(User user);

    /**
     * Updates a myUser.
     *
     * @param user the entity to update.
     * @return the persisted entity.
     */
    User update(User user);

    /**
     * Partially updates a myUser.
     *
     * @param user the entity to update partially.
     * @return the persisted entity.
     */
    Optional<User> partialUpdate(User user);

    /**
     * Get all the myUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Get the "id" myUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<User> findOne(Long id);

    /**
     * Get the "id" myUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<User> findOneEager(Long id);

    /**
     * Delete the "id" myUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Login the provided user by generating a token for it
     *
     * @param user to login
     */
    String login(User user);

    /**
     * Logout the provided user by invalidating the token
     *
     * @param user to logout
     */
    void logout(User user);

    /**
     * validates the token exists
     *
     * @param token to validate
     */
    boolean isTokenValid(String token);

    /**
     * returns the id of the user who owns the token
     *
     * @param token to decode
     */
    String getIdFromToken(String token);
}
