package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.user.*;
import quantum.model.User;
import quantum.security.jwt.JwtUtil;
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
    public ResponseEntity<UserListResponse> getUsers(String token, Pageable pageable) {
        log.info("[CONTROLLER] - Searching users");
        UserListResponse result = service.getUsers(pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * GET to /api/user/{username} to fetch users list.
     *
     * @param username The username
     * @return The list of users
     */
    @Override
    public ResponseEntity<UserResponse> getUser(String token, String username) {
        log.info("[CONTROLLER] - Searching user");
        UserResponse result = service.getUser(username);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/user to create a user.
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
     * PUT to /api/user to update a user.
     *
     * @param body The user body.
     * @return The updated user.
     */
    @Override
    public ResponseEntity<UserResponse> patchUser(String token, UpdateUserBody body) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Updating user");
        UserResponse result = service.updateUser(username, body);
        return ResponseEntity.ok(result);
    }

    /**
     * PUT to /api/user/password to update a user.
     *
     * @param body The user body.
     * @return The updated user.
     */
    @Override
    public ResponseEntity<UserResponse> patchUserPassword(String token, UpdatePasswordBody body) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Updating user password");
        UserResponse result = service.updatePassword(username, body);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE to /api/user to delete a user.
     *
     * @return The deleted user.
     */
    @Override
    public ResponseEntity<Void> deleteUser(String token) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Deleting user");
        service.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

}
