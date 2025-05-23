package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.group.GroupGamesResponse;
import quantum.dto.group.GroupResponse;
import quantum.dto.group.NewGroupBody;
import quantum.dto.group.UpdateGroupBody;
import quantum.dto.userGroups.UserGroupsListResponse;
import quantum.security.jwt.JwtUtil;
import quantum.service.GroupService;
import quantum.web.api.GroupApi;

/**
 * Controller for {@link GroupService} entity.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupController implements GroupApi {

    private final GroupService service;

    /**
     * GET to /api/groups/{group_id} to fetch a user group list.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     * @return The list of groups the user is in or is invited.
     */
    @Override
    public ResponseEntity<GroupGamesResponse> getGroupGames(String token, Long groupId) {
        log.info("[CONTROLLER] - Searching groups games");
        GroupGamesResponse result = service.getGroupGames(groupId);
        return ResponseEntity.ok(result);
    }

    /**
     * PATCH to /api/groups/{groupId} to edit a group.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     * @param body    The group body.
     * @return The edited group.
     */
    @Override
    public ResponseEntity<GroupResponse> patchGroup(String token, Long groupId, UpdateGroupBody body) {
        log.info("[CONTROLLER] - Updating group");
        service.updateGroup(groupId, body);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET to /api/user/groups to fetch a user group list.
     *
     * @param token The token with the authentication information.
     * @return The list of groups the user is in or is invited.
     */
    @Override
    public ResponseEntity<UserGroupsListResponse> getUserGroups(String token) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Searching user groups");
        UserGroupsListResponse result = service.getUserGroups(username);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/user/group to create a group.
     *
     * @param token The token with the authentication information.
     * @param body  The group body.
     * @return The new group.
     */
    @Override
    public ResponseEntity<GroupResponse> postGroup(String token, NewGroupBody body) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Creating group");
        GroupResponse result = service.postGroup(username, body);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/groups/{group_id}/invite/{username} to send an invitation to a user.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @param groupId  The group id.
     */
    @Override
    public ResponseEntity<Void> sendInvite(String token, String username, Long groupId) {
        log.info("[CONTROLLER] - Sending invite to group");
        service.sendInvite(username, groupId);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH to /api/user/groups/{group_id} to accept a group invitation from a user.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     */
    @Override
    public ResponseEntity<Void> joinGroup(String token, Long groupId) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Accepting group invitation");
        service.joinGroup(username, groupId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE to /api/user/groups/{groupId} to delete a group from a user.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     */
    @Override
    public ResponseEntity<Void> deleteUserGroup(String token, Long groupId) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Declining or exiting group");
        service.declineOrExitGroup(username, groupId);
        return ResponseEntity.noContent().build();
    }


    /**
     * PATCH to /api/user/groups/{group_id}/game/{game_id} to vote a group game by a user.
     *
     * @param token   The token with the authentication information.
     * @param groupId The group id.
     * @param gameId  The game id.
     */
    @Override
    public ResponseEntity<Void> voteGroupGame(String token, Long groupId, Long gameId) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Voting game");
        service.voteGroupGame(username, groupId, gameId);
        return ResponseEntity.noContent().build();
    }
}
