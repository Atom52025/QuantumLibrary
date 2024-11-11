package quantum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import quantum.dto.group.*;
import quantum.dto.userGroups.UserGroupsListResponse;
import quantum.exceptions.BadRequestException;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityFoundException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.GamesMapping;
import quantum.mapping.GroupMapping;
import quantum.model.Game;
import quantum.model.Group;
import quantum.model.User;
import quantum.model.UserGroup;
import quantum.repository.GroupRepository;
import quantum.service.GroupService;
import quantum.service.UserGamesService;
import quantum.service.UserGroupsService;
import quantum.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;

/**
 * Service implementation for {@link Group} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;
    private final UserGroupsService userGroupsService;
    private final UserGamesService userGamesService;
    private final UserService userService;
    private final GroupMapping mapper;
    private final GamesMapping gameMapper;

    //------------------------------------- PUBLIC METHODS -------------------------------------//


    /**
     * Gets the group games.
     *
     * @param groupId The group id.
     * @return the group games
     */
    @Override
    public GroupGamesResponse getGroupGames(Long groupId) {
        // Search for the group
        Group group = findGroupById(groupId);

        // Gets user groups
        List<UserGroup> userGroups = group.getUserGroups();

        // Gets all accepted users in the group
        List<User> users = userGroupsService.findAcceptedUsersInGroup(groupId, true);

        // Gets the common games
        Set<Game> commonGames = userGamesService.getCommonGames(users);

        // Map of game IDs to users who voted for them
        Map<Long, List<String>> gameVotesMap = userGroups.stream()
                .flatMap(userGroup -> userGroup.getVoted().stream()
                        .map(gameId -> Map.entry(gameId, userGroup.getUser().getUsername()))
                )
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        // Create the VotedGamesResponse using the gameVotesMap
        List<VotedGamesResponse> votedGamesResponses = commonGames.stream()
                .map(game -> {
                    List<String> voters = gameVotesMap.getOrDefault(game.getId(), new ArrayList<>());
                    return gameMapper.map(game, voters);
                })
                .collect(Collectors.toList());

        return GroupGamesResponse.builder()
                .group(group)
                .games(votedGamesResponses)
                .build();
    }


    /**
     * Gets the groups the user is in or is invited.
     *
     * @param username The username.
     * @return the groups
     */
    @Override
    public UserGroupsListResponse getUserGroups(String username) {
        return UserGroupsListResponse.builder()
                .accepted(userGroupsService.findAcceptedGroupsByUser(username, true).stream().map(mapper::map).toList())
                .pending(userGroupsService.findAcceptedGroupsByUser(username, false).stream().map(mapper::map).toList())
                .build();
    }


    /**
     * Send invitation to group.
     *
     * @param username The username.
     * @param groupId  The group id.
     * @return The user group.
     */
    @Override
    public UserGroup sendInvite(String username, Long groupId) {

        // Search for the group and the user
        Group group = findGroupById(groupId);
        User user = userService.findUser(username);

        // Check if the user is already in the group
        if (findUsersInGroup(group).contains(user)) {
            throw new EntityFoundException("User already in group");
        }

        // Create and save a new user group
        return userGroupsService.postUserGroup(user, group);
    }


    /**
     * Join group if invited.
     *
     * @param username The username.
     * @param groupId  The groupId.
     */
    @Override
    public void joinGroup(String username, Long groupId) {
        UserGroup userGroup = userGroupsService.findUserGroup(username, groupId);
        userGroup.setAccepted(true);
        userGroupsService.updateUserGroup(userGroup);
    }

    /**
     * Decline or exit group.
     *
     * @param username The username.
     * @param groupId  The group id.
     */
    @Override
    public void declineOrExitGroup(String username, Long groupId) {
        // Search for the group
        Group group = findGroupById(groupId);

        // Removes the user from the group
        if (!group.getUserGroups().removeIf(userGroup -> userGroup.getUser().getUsername().equals(username))) {
            throw new EntityNotFoundException();
        }

        // Saves the group and cascades to the user group
        log.info("[SERVICE] - [SAVING GROUP] - Deleting UserGroup from Group: {}", groupId);
        repository.save(group);

        // If the group is empty, delete it
        if (findUsersInGroup(group).isEmpty()) {
            deleteGroup(group);
        }
    }

    /**
     * Gets groups.
     *
     * @return The groups
     */
    @Override
    public GroupListResponse getGroups() {
        List<Group> result;

        try {
            log.info("[SERVICE] - [GROUP SEARCH] - Searching groups");
            result = repository.findAll();
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (result.isEmpty()) {
            throw new EntityNotFoundException();
        }

        // Map entity to response and return
        return GroupListResponse.builder()
                .groups(result.stream().map(mapper::map).toList())
                .build();
    }

    /**
     * Find a group by id.
     *
     * @param id The id of the group to find.
     * @return The group.
     */
    @Override
    public Group findGroupById(Long id) {
        Optional<Group> group;
        try {
            log.info("[SERVICE] - [GROUP SEARCH] - Searching group: {}", id);
            group = repository.findById(id);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (group.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return group.get();
    }

    /**
     * Create a new group.
     *
     * @param username The username.
     * @param body     The body.
     * @return The group.
     */
    @Override
    public GroupResponse postGroup(String username, NewGroupBody body) {
        if (body.getName() == null || body.getName().isEmpty()) {
            throw new BadRequestException("Name is required");
        }

        // Generate new group
        Group newGroup = Group.builder().name(body.getName()).build();

        try {
            // Create the user group for the creator and the invited users
            User creator = userService.findUser(username);
            List<UserGroup> userGroups = new ArrayList<>();
            userGroups.add(UserGroup.builder()
                    .user(creator)
                    .group(newGroup)
                    .accepted(true)
                    .build());

            for (String invitedUser : body.getInvitedUsers()) {
                try {
                    User user = userService.findUser(invitedUser);
                    userGroups.add(UserGroup.builder()
                            .user(user)
                            .group(newGroup)
                            .accepted(false)
                            .build());
                } catch (EntityNotFoundException ex) {
                    log.info("[SERVICE] - [USER GROUP INVITATION] - User not found: {}", invitedUser);
                }
            }

            // Save the user groups
            newGroup.setUserGroups(userGroups);

            log.info("[SERVICE] - [GROUP CREATION] - Saving group: {}", newGroup);
            newGroup = repository.save(newGroup);

        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseConnectionException(DATA_INTEGRITY_ERROR, ex);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        return mapper.map(newGroup);
    }

    /**
     * Patch group.
     *
     * @param groupId The id
     * @param body    The body
     */
    @Override
    public GroupResponse updateGroup(Long groupId, UpdateGroupBody body) {
        // Find the group
        Group groupToUpdate = findGroupById(groupId);

        // Update the group content
        updateGroupContent(body, groupToUpdate);

        try {
            log.info("[SERVICE] - [GAME UPDATE] - Saving group: {}", groupToUpdate);
            groupToUpdate = repository.save(groupToUpdate);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(groupToUpdate);
    }


    /**
     * Vote group game.
     *
     * @param username The username
     * @param groupId  The group id
     * @param gameId   The game id
     */
    public void voteGroupGame(String username, Long groupId, Long gameId) {

        UserGroup userGroup = userGroupsService.findUserGroup(username, groupId);

        List<Long> voted = new ArrayList<>(userGroup.getVoted());

        if (voted.contains(gameId)) {
            voted.remove(gameId);
            userGroup.setVoted(voted);
        } else {
            voted.add(gameId);
            userGroup.setVoted(voted);
        }

        try {
            log.info("[SERVICE] - [USER GROUP VOTE] - Voting game on user group: {}", userGroup);
            userGroupsService.updateUserGroup(userGroup);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    //------------------------------------- PRIVATE METHODS -------------------------------------//

    /**
     * Delete group.
     *
     * @param group The group.
     */
    public void deleteGroup(Group group) {
        try {
            log.info("[SERVICE] - [GROUP DELETE] - Delete group: {}", group);
            repository.delete(group);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    /**
     * Update a group.
     *
     * @param body The body
     */
    private void updateGroupContent(UpdateGroupBody body, Group groupToUpdate) {
        if (body.getName() != null) {
            groupToUpdate.setName(body.getName());
        }
    }

    /**
     * Gets all users in a specific group.
     *
     * @param group The group.
     * @return the groups
     */
    private List<User> findUsersInGroup(Group group) {
        log.info("[SERVICE] - [GROUP SEARCH] - Search users in group: {}", group.getId());
        return group.getUserGroups().stream().map(UserGroup::getUser).toList();
    }


}
