package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quantum.dto.group.GroupGamesResponse;
import quantum.dto.group.GroupResponse;
import quantum.dto.group.NewGroupBody;
import quantum.dto.group.UpdateGroupBody;
import quantum.dto.userGroups.UserGroupsListResponse;
import quantum.model.Group;

/**
 * The api interface for {@link Group} entity.
 */

@Validated
@RequestMapping("/api")
public interface GroupApi {

    /**
     * GET to /api/group/{group_id} to fetch games of a group.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     * @return The list of games of a group.
     */
    @Operation(summary = "Get group games", description = "Get group games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of group games"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/group/{group_id}", produces = "application/json")
    ResponseEntity<GroupGamesResponse> getGroupGames(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The group id")
            @PathVariable("group_id")
            Long groupId);

    /**
     * PATCH to /api/groups/{group_id} to edit a group.
     *
     * @param groupId The group id.
     * @param body    The group body.
     * @return The edited group.
     */
    @Operation(summary = "Edit a group", description = "Edit a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of groups"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/group/{group_id}", produces = "application/json")
    ResponseEntity<GroupResponse> patchGroup(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The group id")
            @PathVariable("group_id")
            Long groupId,
            @Valid @RequestBody
            UpdateGroupBody body
    );

    /**
     * POST to /api/groups/{group_id}/invite/{username} to send an invitation to a user.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @param groupId  The group id.
     */
    @Operation(summary = "Sends invitation to user", description = "Create user game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/group/{group_id}/invite/{username}", produces = "application/json")
    ResponseEntity<Void> sendInvite(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The username")
            @PathVariable("username")
            String username,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The group id")
            @PathVariable("group_id")
            Long groupId
    );

    /**
     * GET to /api/user/groups to create a group.
     *
     * @return The new group.
     */
    @Operation(summary = "Get groups from user", description = "Get groups from user", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of groups"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/user/groups", produces = "application/json")
    ResponseEntity<UserGroupsListResponse> getUserGroups(
            @RequestHeader("Authorization")
            String token
    );

    /**
     * POST to /api/user/groups to create a group.
     *
     * @param body The group body.
     * @return The new group.
     */
    @Operation(summary = "Creates a group", description = "Creates a group", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The group created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/user/group", produces = "application/json")
    ResponseEntity<GroupResponse> postGroup(
            @RequestHeader("Authorization")
            String token,
            @Valid @RequestBody
            NewGroupBody body
    );

    /**
     * PATCH to /api/user/groups/{group_id} to accept a group invitation from a user.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     */
    @Operation(summary = "Edit a user group", description = "Edit a user group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/user/group/{group_id}", produces = "application/json")
    ResponseEntity<Void> joinGroup(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The group id")
            @PathVariable("group_id")
            Long groupId
    );

    /**
     * DELETE to /api/user/groups/{group_id} to delete a group from a user.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     */
    @Operation(summary = "Delete a user group", description = "Delete a user group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/user/group/{group_id}", produces = "application/json")
    ResponseEntity<Void> deleteUserGroup(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The group id")
            @PathVariable("group_id")
            Long groupId
    );

    /**
     * PATCH to /api/user/groups/{group_id}/game/{game_id} to vote a group game by a user.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     * @param gameId  The game id.
     */
    @Operation(summary = "Vote game on user group", description = "Vote game on user group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/user/group/{group_id}/game/{game_id}", produces = "application/json")
    ResponseEntity<Void> voteGroupGame(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The group id")
            @PathVariable("group_id")
            Long groupId,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The game id")
            @PathVariable("game_id")
            Long gameId);
}
