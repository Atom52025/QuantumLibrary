package quantum.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import quantum.dto.user.*;
import quantum.model.User;

/**
 * Service for {@link User} entity.
 */
public interface UserService {

    /**
     * Gets users.
     *
     * @param pageable The pageable.
     * @return The users.
     */
    UserListResponse getUsers(Pageable pageable) throws Exception;

    /**
     * Gets users.
     *
     * @param username The username.
     * @return The users.
     */
    UserResponse getUser(String username);

    /**
     * Post user.
     *
     * @param body The body
     */
    UserResponse postUser(NewUserBody body);

    /**
     * Patch user.
     *
     * @param username The username of the user.
     * @param body     The body,
     */
    UserResponse updateUser(String username, UpdateUserBody body);

    /**
     * Update password.
     *
     * @param username The username of the user.
     * @param body     The update password body.
     * @return The user
     */
    UserResponse updatePassword(String username, UpdatePasswordBody body);

    /**
     * Delete user.
     *
     * @param username The username of the user.
     */
    void deleteUser(String username);

    /**
     * Find a user by username.
     *
     * @param username The username of the user to find.
     * @return The user.
     */
    User findUser(String username);

    /**
     * Gets user.
     *
     * @param username The username of the user.
     * @return The user
     */
    UserDetails loadUserByUsername(String username);

}
