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
     * PATCH to /api/groups/{groupId} to edit a group.
     *
     * @param token    The token with the authentication information.
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
     * GET to /api/groups/{group_id} to fetch a user group list.
     *
     * @param token    The token with the authentication information.
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
     * GET to /api/user/{username}/groups to fetch a user group list.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @return The list of groups the user is in or is invited.
     */
    @Override
    public ResponseEntity<UserGroupsListResponse> getUserGroups(String token, String username) {
        log.info("[CONTROLLER] - Searching user groups");
        UserGroupsListResponse result = service.getUserGroups(username);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/groups to create a group.
     *
     * @param token The token with the authentication information.
     * @param username The username.
     * @param body  The group body.
     * @return The new group.
     */
    @Override
    public ResponseEntity<GroupResponse> postGroup(String token, String username, NewGroupBody body) {
        log.info("[CONTROLLER] - Creating group");
        GroupResponse result = service.postGroup(username, body);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/user/{username}/groups to send an invitation to a user.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @param groupId The group id.
     */
    @Override
    public ResponseEntity<Void> sendInvite(String token, String username, Long groupId) {
        log.info("[CONTROLLER] - Sending invite to group");
        service.sendInvite(username, groupId);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH to /api/user/{username}/groups/{groupId} to accept a group invitation from a user.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @param groupId The group id.
     */
    @Override
    public ResponseEntity<Void> joinGroup(String token, String username, Long groupId) {
        log.info("[CONTROLLER] - Accepting group invitation");
        service.joinGroup(username, groupId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE to /api/user/{username}/groups/{groupId} to delete a group from a user.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @param groupId The group id.
     */
    @Override
    public ResponseEntity<Void> deleteUserGroup(String token, String username, Long groupId) {
        log.info("[CONTROLLER] - Declining or exiting group");
        service.declineOrExitGroup(username, groupId);
        return ResponseEntity.noContent().build();
    }
}
