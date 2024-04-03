package quantum.service.impl;

import quantum.exceptions.EntityNotFoundException;
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
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
import quantum.dto.user.UserListResponse;
import quantum.dto.user.UserResponse;
import quantum.exceptions.DatabaseConnectionException;
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

    private final UserRepository userRepository;
    private final UsersMapping mapper;

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
            result = userRepository.findAll(pageable);
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
     * Create a new user.
     *
     * @param body The body
     * @return The user
     */
    @Override
    public UserResponse postUser(NewUserBody body) {

        // Generate new user
        User newUser = generateNewUser(body);

        try {
            log.debug("[GAME CREATION] - Saving user: {}", newUser);
            newUser = userRepository.save(newUser);
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
            log.debug("[GAME UPDATE] - Saving user: {}", userToUpdate);
            userToUpdate = userRepository.save(userToUpdate);
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
            userRepository.delete(findUser(username));
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
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
                .build();
    }

    /**
     * Update a user.
     *
     * @param userToUpdate The user to update.
     * @param body         The body
     */
    private void updateUserContent(UpdateUserBody body, User userToUpdate) {
        if (body.getUsername() != null) {
            userToUpdate.setUsername(body.getUsername());
        }
        if (body.getEmail() != null) {
            userToUpdate.setEmail(body.getEmail());
        }
        if (body.getPassword() != null) {
            userToUpdate.setPassword(BCrypt.hashpw(body.getPassword(), BCrypt.gensalt()));
        }
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
            user = userRepository.findByUsername(username);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (user.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return user.get();
    }
}
