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
        * @param pageable the pageable
        * @return the users
        */
        UserListResponse getUsers(Pageable pageable);

        /**
         * Post user.
         * @param body the body
         */
        UserResponse postUser(NewUserBody body);

        /**
         * Patch user.
         * @param id the id
         * @param body the body
         */
        UserResponse updateUser(Long id, UpdateUserBody body);

        /**
         * Delete user.
         * @param id the id
         */
        void deleteUser(Long id);

        /**
         * Gets user.
         * @param username The user username.
         * @return The user
         */
        UserDetails loadUserByUsername(String username);

}
