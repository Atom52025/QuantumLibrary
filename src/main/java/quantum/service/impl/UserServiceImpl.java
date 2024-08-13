package quantum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import quantum.dto.user.*;
import quantum.exceptions.BadPasswordException;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.UsersMapping;
import quantum.model.User;
import quantum.repository.UserRepository;
import quantum.service.UserService;

import java.util.Optional;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;

/**
 * Service implementation for {@link User} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UsersMapping mapper;

    //------------------------------------- PUBLIC METHODS -------------------------------------//

    /**
     * Retrieve Users.
     *
     * @param pageable the pageable
     * @return the users
     */
    @Override
    public UserListResponse getUsers(Pageable pageable) {
        Page<User> result;

        try {
            log.info("[SERVICE] - [USER SEARCH] - Searching users");
            result = repository.findAll(pageable);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }


        // Check if there is any result
        if (result.isEmpty()) {
            throw new EntityNotFoundException();
        }

        // Map entity to response and return
        return UserListResponse.builder()
                .users(result.get().map(mapper::map).toList())
                .build();
    }

    /**
     * Retrieve User.
     *
     * @param username The username.
     * @return The user.
     */
    @Override
    public UserResponse getUser(String username) {
        Optional<User> result;

        try {
            log.info("[SERVICE] - [USER SEARCH] - Searching user");
            result = repository.findByUsername(username);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }


        // Check if there is any result
        if (result.isEmpty()) {
            throw new EntityNotFoundException();
        }

        // Map entity to response and return
        return mapper.map(result.get());
    }

    /**
     * Find a user by username.
     *
     * @param username The username of the user to find.
     * @return The user.
     */
    public User findUser(String username) {
        Optional<User> user;
        try {
            log.info("[SERVICE] - [USER SEARCH] - Searching user: {}", username);
            user = repository.findByUsername(username);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (user.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return user.get();
    }

    /**
     * Load a user by its username for the authentication process.
     *
     * @param username The username
     * @return The user details
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    /**
     * Create a new user.
     *
     * @param body The body
     * @return The user
     */
    @Override
    public UserResponse postUser(NewUserBody body) {

        // Check if the user already exists
        if (checkUser(body.getUsername())) {
            throw new DataIntegrityViolationException("User already exists");
        }

        // Generate new user
        User newUser = generateNewUser(body);

        try {
            log.info("[SERVICE] - [USER CREATION] - Saving user: {}", newUser);
            newUser = repository.save(newUser);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseConnectionException(DATA_INTEGRITY_ERROR, ex);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(newUser);
    }

    /**
     * Update a user.
     *
     * @param username The username of the user.
     * @param body     The body
     * @return The user
     */
    @Override
    public UserResponse updateUser(String username, UpdateUserBody body) {

        // Find the user
        User userToUpdate = findUser(username);

        // Update the user content
        updateUserContent(body, userToUpdate);

        try {
            log.info("[SERVICE] - [USER UPDATE] - Saving user: {}", userToUpdate);
            userToUpdate = repository.save(userToUpdate);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(userToUpdate);
    }

    /**
     * Update password.
     *
     * @param username The username of the user.
     * @param body     The update password body.
     * @return The user
     */
    @Override
    public UserResponse updatePassword(String username, UpdatePasswordBody body) {

        // Find the user
        User userToUpdate = findUser(username);

        // Check if the old password is correct
        if (checkPassword(userToUpdate, body.getOldPassword())) {
            throw new BadPasswordException();
        }

        // Update the user content
        userToUpdate.setPassword(BCrypt.hashpw(body.getNewPassword(), BCrypt.gensalt()));

        try {
            log.info("[SERVICE] - [USER UPDATE] - Saving user: {}", userToUpdate);
            userToUpdate = repository.save(userToUpdate);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(userToUpdate);
    }

    /**
     * Delete a user.
     *
     * @param username The username
     */
    @Override
    public void deleteUser(String username) {
        try {
            log.info("[SERVICE] - [GAME DELETE] - Deleting user: {}", username);
            repository.delete(findUser(username));
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    //------------------------------------- PRIVATE METHODS -------------------------------------//

    /**
     * Check password.
     *
     * @param user The user.
     * @param password The password.
     * @return Whether the password is correct or not.
     */
    public boolean checkPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    /**
     * Generate a new user.
     *
     * @param body The body
     * @return The user
     */
    private User generateNewUser(NewUserBody body) {
        return User.builder()
                .username(body.getUsername())
                .email(body.getEmail())
                .role("USER")
                .password(BCrypt.hashpw(body.getPassword(), BCrypt.gensalt()))
                .image("https://api.dicebear.com/7.x/identicon/svg?seed=" + body.getUsername())
                .build();
    }

    /**
     * Find if a user exist with given username.
     *
     * @param username The username of the user to find.
     * @return User found.
     */
    public boolean checkUser(String username) {
        Optional<User> user;
        try {
            log.info("[SERVICE] - [USER SEARCH] - Searching user: {}", username);
            user = repository.findByUsername(username);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        return !user.isEmpty();
    }

    /**
     * Update a user.
     *
     * @param userToUpdate The user to update.
     * @param body         The body
     */
    private void updateUserContent(UpdateUserBody body, User userToUpdate) {
        if (body.getEmail() != null) {
            userToUpdate.setEmail(body.getEmail());
        }
        if (body.getImage() != null) {
            userToUpdate.setImage(body.getImage());
        }
    }


}
