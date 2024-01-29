package quantum.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import quantum.dto.user.DataListResponseUser;
import quantum.dto.user.DataResponseUser;
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
import quantum.exceptions.DatabaseConnectionException;
import quantum.mapping.UsersMapping;
import quantum.model.User;
import quantum.repository.UserRepository;
import quantum.service.UserService;

import java.util.Optional;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;
import static quantum.constant.ErrorConstants.ENTITY_NOT_FOUND_ERROR;

/**
 * Service implementation for {@link User} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UsersMapping mapper;

    // TODO: Manual id generation

    /**
     * Retrieve Users.
     * @param pageable the pageable
     * @return the users
     */
    @Override
    public DataListResponseUser getUsers(Pageable pageable) {
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

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User userDetails = (User) authentication.getPrincipal();

        // Map entity to response and return
        return DataListResponseUser.builder()
                .users(result.get().map(mapper::map).toList())
                .username(userDetails.getUsername())
                .build();
    }

    /**
     * Create a new user.
     * @param body the body
     * @return the user
     */
    @Override
    public DataResponseUser postUser(NewUserBody body) {

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
     * @param id the id
     * @param body the body
     * @return the user
     */
    @Override
    public DataResponseUser updateUser(Long id, UpdateUserBody body) {

        // Find the user
        User userToUpdate = findUserById(id);

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
     * @param id the id
     */
    @Override
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(validateUser(id));
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
     * Make sure a user exists.
     * @param id The id of the user to validate.
     */
    private Long validateUser(Long id) {
        boolean exists;
        try {
            exists = userRepository.existsById(id);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        if (!exists) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR);
        }
        return id;
    }

    /**
     * Generate a new user.
     * @param body the body
     * @return the user
     */
    private User generateNewUser(NewUserBody body) {
        return User.builder()
                .username(body.getName())
                .email(body.getEmail())
                .build();
    }

    /**
     * Update a user.
     *
     * @param body the body
     */
    private void updateUserContent(UpdateUserBody body, User userToUpdate) {
        if (body.getName() != null) {
            userToUpdate.setUsername(body.getName());
        }
        if (body.getEmail() != null) {
            userToUpdate.setEmail(body.getEmail());
        }
    }

    /**
     * Find a user by id.
     * @param id The id of the user to find.
     * @return The user.
     */
    private User findUserById(Long id) {
        Optional<User> user;
        try{
            user = userRepository.findById(id);
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
