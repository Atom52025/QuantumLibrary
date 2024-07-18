package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
import quantum.dto.user.UserListResponse;
import quantum.dto.user.UserResponse;
import quantum.model.User;
import quantum.service.UserService;
import quantum.web.api.UserApi;

/**
 * Controller for {@link User} entity.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService service;

    /**
     * GET to /api/users to fetch users list.
     *
     * @param pageable the pageable
     * @return The list of users
     */
    @Override
    public ResponseEntity<UserListResponse> getUsers(String token, Pageable pageable) throws Exception {
        log.info("[CONTROLLER] - Searching users");
        UserListResponse result = service.getUsers(pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * GET to /api/users to fetch users list.
     *
     * @param username The username of the user to update.
     * @return The list of users
     */
    @Override
    public ResponseEntity<UserResponse> getUser(String token, String username) {
        log.info("[CONTROLLER] - Searching user");
        UserResponse result = service.getUser(username);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/users to create a user.
     *
     * @param body The user body.
     * @return The new user.
     */
    @Override
    public ResponseEntity<UserResponse> postUser(NewUserBody body) {
        log.info("[CONTROLLER] - Creating user");
        UserResponse result = service.postUser(body);
        return ResponseEntity.ok(result);
    }

    /**
     * PUT to /api/users/{username} to update a user.
     *
     * @param username The username of the user to update.
     * @param body     The user body.
     * @return The updated user.
     */
    @Override
    public ResponseEntity<UserResponse> patchUser(String token, String username, UpdateUserBody body) {
        log.info("[CONTROLLER] - Updating user");
        UserResponse result = service.updateUser(username, body);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE to /api/users/{username} to delete a user.
     *
     * @param username The username of the user to delete.
     * @return The deleted user.
     */
    @Override
    public ResponseEntity<Void> deleteUser(String token, String username) {
        log.info("[CONTROLLER] - Deleting user");
        service.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

}
