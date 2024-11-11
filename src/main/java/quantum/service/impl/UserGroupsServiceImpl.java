package quantum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.GamesMapping;
import quantum.mapping.UsersMapping;
import quantum.model.Group;
import quantum.model.User;
import quantum.model.UserGroup;
import quantum.repository.UserGroupsRepository;
import quantum.service.GroupService;
import quantum.service.UserGroupsService;
import quantum.service.UserService;

import java.util.List;
import java.util.Optional;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;
import static quantum.constant.ErrorConstants.ENTITY_NOT_FOUND_ERROR;


/**
 * Service implementation for {@link UserGroup} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserGroupsServiceImpl implements UserGroupsService {

    private final UserGroupsRepository repository;

    //------------------------------------- PUBLIC METHODS -------------------------------------//

    /**
     * Create a new user group.
     *
     * @param user  The user.
     * @param group The group.
     * @return The user group.
     */
    @Override
    public UserGroup postUserGroup(User user, Group group) {
        UserGroup newUserGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .accepted(false)
                .build();

        try {
            log.info("[SERVICE] - [USER GROUP CREATION] - Saving user group: {}", newUserGroup);
            return repository.save(newUserGroup);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseConnectionException(DATA_INTEGRITY_ERROR, ex);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    /**
     * Update a user group.
     *
     * @param userGroup The user group.
     */
    @Override
    public void updateUserGroup(UserGroup userGroup) {
        try {
            log.info("[SERVICE] - [USER GROUP UPDATE] - Saving user group: {}", userGroup);
            repository.save(userGroup);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    /**
     * Delete a user group.
     *
     * @param username The username.
     * @param groupId  The group id.
     */
    @Override
    public void deleteUserGroup(String username, Long groupId) {
        // Find the user group
        UserGroup userGroupToDelete = findUserGroup(username, groupId);

        // Deletes the user group
        try {
            log.info("[SERVICE] - [USER GROUP DELETE] - Deleting user group: {}", userGroupToDelete);
            repository.delete(userGroupToDelete);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    /**
     * Gets accepted users in a specific group.
     *
     * @param groupId  The group id.
     * @param accepted The accepted value.
     * @return the groups
     */
    public List<User> findAcceptedUsersInGroup(Long groupId, boolean accepted) {
        List<UserGroup> result;

        try {
            log.info("[SERVICE] - [USER GROUP SEARCH] - Searching users in group: {}", groupId);
            result = repository.findByGroup_IdAndAccepted(groupId, accepted);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return result.stream().map(UserGroup::getUser).toList();
    }

    /**
     * Gets accepted users in a specific group.
     *
     * @param username The username.
     * @param accepted The accepted value.
     * @return the groups
     */
    public List<Group> findAcceptedGroupsByUser(String username, boolean accepted) {
        List<UserGroup> result;

        try {
            log.info("[SERVICE] - [USER GROUP SEARCH] - Searching groups in user: {}", username);
            result = repository.findByUser_UsernameAndAccepted(username, accepted);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return result.stream().map(UserGroup::getGroup).toList();
    }

    /**
     * Find a user game by group id and username.
     *
     * @param groupId  The group id.
     * @param username The username.
     * @return The user game.
     */
    public UserGroup findUserGroup(String username, Long groupId) {
        Optional<UserGroup> userGroup;

        // Try to find the entity
        try {
            log.info("[SERVICE] - [USER GROUP SEARCH] - Searching user group of: {} with id: {}", username, groupId);
            userGroup = repository.findByUser_UsernameAndGroup_Id(username, groupId);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (userGroup.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR);
        }

        return userGroup.get();
    }
}
