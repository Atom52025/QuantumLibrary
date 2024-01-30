package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.user.UserListResponse;
import quantum.dto.user.UserResponse;
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
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

    private  final UserService userService;

    /**
     * GET to /api/users to fetch users list.
     * @param pageable the pageable
     * @return The list of users
     */
    @Override
    public ResponseEntity<UserListResponse> getUsers(String token, Pageable pageable) {
        log.info("[CONTROLLER] - Searching users");
        UserListResponse result = userService.getUsers(pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/users to create a user.
     * @param body The user body.
     * @return The new user.
     */
    @Override
    public ResponseEntity<UserResponse> postUser(String token, NewUserBody body) {
        log.info("[CONTROLLER] - Creating user");
        UserResponse result = userService.postUser(body);
        return ResponseEntity.ok(result);
    }

    /**
     * PUT to /api/users/{id} to update a user.
     * @param id The id of the user to update.
     * @param body The user body.
     * @return The updated user.
     */
    @Override
    public ResponseEntity<UserResponse> patchUser(String token, Long id, UpdateUserBody body) {
        log.info("[CONTROLLER] - Updating user");
        UserResponse result = userService.updateUser(id, body);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE to /api/users/{id} to delete a user.
     * @param id The id of the user to delete.
     * @return The deleted user.
     */
    @Override
    public ResponseEntity<Void> deleteUser(String token, Long id) {
        log.info("[CONTROLLER] - Deleting user");
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
