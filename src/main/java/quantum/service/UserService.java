package quantum.service;

import org.springframework.data.domain.Pageable;
import quantum.dto.user.DataListResponseUser;
import quantum.dto.user.DataResponseUser;
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
        DataListResponseUser getUsers(Pageable pageable);

        /**
         * Post user.
         * @param body the body
         */
        DataResponseUser postUser(NewUserBody body);

        /**
         * Patch user.
         * @param id the id
         * @param body the body
         */
        DataResponseUser updateUser(Long id, UpdateUserBody body);

        /**
         * Delete user.
         * @param id the id
         */
        void deleteUser(Long id);

}
