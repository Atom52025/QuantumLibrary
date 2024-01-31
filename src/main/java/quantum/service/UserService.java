package quantum.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import quantum.dto.user.UserListResponse;
import quantum.dto.user.UserResponse;
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
import quantum.model.User;

/**
 * Service for {@link User} entity.
 */
public interface UserService {

        /**
        * Gets users.
        * @param pageable The pageable.
        * @return The users.
        */
        UserListResponse getUsers(Pageable pageable);

        /**
         * Post user.
         * @param body the body
         */
        UserResponse postUser(NewUserBody body);

        /**
         * Patch user.
         * @param username The username of the user.
         * @param body The body,
         */
        UserResponse updateUser(String username, UpdateUserBody body);

        /**
         * Delete user.
         * @param username The username of the user.
         */
        void deleteUser(String username);

        /**
         * Find a user by username.
         * @param username The username of the user to find.
         * @return The user.
         */
        User findUser(String username);

        /**
         * Gets user.
         * @param username The username of the user.
         * @return The user
         */
        UserDetails loadUserByUsername(String username);

}
