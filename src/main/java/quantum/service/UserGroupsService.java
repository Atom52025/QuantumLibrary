package quantum.service;

import quantum.model.Group;
import quantum.model.User;
import quantum.model.UserGroup;

import java.util.List;

/**
 * Service for {@link Group} entity.
 */
public interface UserGroupsService {
    /**
     * Accept group.
     *
     * @param user  The user.
     * @param group The group.
     * @return the user group
     */
    UserGroup postUserGroup(User user, Group group);

    /**
     * Update a user group.
     *
     * @param username The username.
     * @param groupId  The group id.
     */
    void updateUserGroup(String username, Long groupId);

    /**
     * Decline group.
     *
     * @param username The username.
     * @param groupId  The group id.
     */
    void deleteUserGroup(String username, Long groupId);


    /**
     * Gets accepted users in a specific group.
     *
     * @param groupId  The group id.
     * @param accepted The accepted value.
     * @return The users.
     */
    List<User> findAcceptedUsersInGroup(Long groupId, boolean accepted);

    /**
     * Gets accepted users in a specific group.
     *
     * @param username The username.
     * @param accepted The accepted value.
     * @return The groups.
     */
    List<Group> findAcceptedGroupsByUser(String username, boolean accepted);

}
