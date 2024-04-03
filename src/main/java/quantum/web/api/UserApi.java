package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
import quantum.dto.user.UserListResponse;
import quantum.dto.user.UserResponse;
import quantum.model.User;

/**
 * The api interface for {@link User} entity.
 */

@Validated
public interface UserApi {

    /**
     * GET to /api/users to fetch users list.
     *
     * @param pageable the pageable
     * @return The list of users
     */
    @Operation(summary = "Get users list", description = "Get users list", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of users"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/api/users", produces = "application/json")
    ResponseEntity<UserListResponse> getUsers(
            @RequestHeader("Authorization")
            String token,
            @PageableDefault
            Pageable pageable
    ) throws Exception;

    /**
     * POST to /api/users to create a user.
     *
     * @param body The user body.
     * @return The new user.
     */
    @Operation(summary = "Get users list", description = "Get users list", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of users"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/api/users", produces = "application/json")
    ResponseEntity<UserResponse> postUser(
            @Valid @RequestBody
            NewUserBody body
    );

    /**
     * PATCH to /api/users/{username} to edit a user.
     *
     * @param username The user id.
     * @param body     The user body.
     * @return The edited user.
     */
    @Operation(summary = "Edit a user", description = "Edit a user", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of users"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/api/users/{username}", produces = "application/json")
    ResponseEntity<UserResponse> patchUser(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user id")
            @PathVariable("username")
            String username,
            @Valid @RequestBody
            UpdateUserBody body
    );

    /**
     * DELETE to /api/users/{username} to delete a user.
     *
     * @param username The user id
     */
    @Operation(summary = "Delete a user", description = "Delete a user", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/api/users/{username}", produces = "application/json")
    ResponseEntity<Void> deleteUser(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user id")
            @PathVariable("username")
            String username
    );
}
