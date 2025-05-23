package quantum.service;

import quantum.dto.group.*;
import quantum.dto.userGroups.UserGroupsListResponse;
import quantum.model.Group;
import quantum.model.UserGroup;

/**
 * Service for {@link Group} entity.
 */
public interface GroupService {

    /**
     * Gets groups.
     *
     * @return The groups
     */
    GroupListResponse getGroups();

    /**
     * Gets the group games.
     *
     * @param groupId The group id.
     * @return the group games
     */
    GroupGamesResponse getGroupGames(Long groupId);

    /**
     * Gets the groups the user is in or is invited.
     *
     * @param username The username.
     * @return the groups
     */
    UserGroupsListResponse getUserGroups(String username);

    /**
     * Send invitation to group.
     *
     * @param username The username.
     * @param groupId  The group id.
     * @return The user group.
     */
    UserGroup sendInvite(String username, Long groupId);

    /**
     * Join group if invited.
     *
     * @param username The username.
     * @param groupId  The groupId.
     */
    void joinGroup(String username, Long groupId);

    /**
     * Decline or exit group.
     *
     * @param username The username.
     * @param groupId  The group id.
     */
    void declineOrExitGroup(String username, Long groupId);

    /**
     * Create a new group.
     *
     * @param username The username.
     * @param body     The body.
     * @return The group.
     */
    GroupResponse postGroup(String username, NewGroupBody body);

    /**
     * Patch group.
     *
     * @param groupId The id
     * @param body    The body
     */
    GroupResponse updateGroup(Long groupId, UpdateGroupBody body);


    /**
     * Vote group game.
     *
     * @param username The username
     * @param groupId  The group id
     * @param gameId   The game id
     */
    void voteGroupGame(String username, Long groupId, Long gameId);
}
