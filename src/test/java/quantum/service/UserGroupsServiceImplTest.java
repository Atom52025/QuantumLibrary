package quantum.service.impl;

import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;
import quantum.model.Game;
import quantum.model.Group;
import quantum.model.User;
import quantum.model.UserGroup;
import quantum.repository.UserGroupsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static quantum.constant.TestConstants.*;

/**
 * Unit tests for {@link UserGroupsServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class UserGroupsServiceImplTest {

    @Mock
    private UserGroupsRepository repository;

    @InjectMocks
    private UserGroupsServiceImpl service;

    /**
     * Test for {@link UserGroupsServiceImpl#postUserGroup} method.
     */
    @Test
    @DisplayName("Test postUserGroup method (OK)")
    void postUserGroupOK() {
        // Clone the entity so the test doesn't modify the original object
        Group group = Group.builder().id(5L).userGroups(new ArrayList<>()).build();

        // Mock repository
        when(repository.save(any(UserGroup.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Verify result
        UserGroup result = service.postUserGroup(SAMPLE_USER, group);

        assertEquals(SAMPLE_USER, result.getUser());
        assertEquals(group, result.getGroup());
        assertFalse(result.getAccepted());
    }

    /**
     * Test for {@link UserGroupsServiceImpl#postUserGroup} method.
     */
    @Test
    @DisplayName("Test postUserGroup method (DataIntegrityViolationException)")
    void postUserGroupDataIntegrityViolationException() {
        when(repository.save(any(UserGroup.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(DatabaseConnectionException.class, () -> service.postUserGroup(SAMPLE_USER, SAMPLE_GROUP));
    }

    /**
     * Test for {@link UserGroupsServiceImpl#postUserGroup} method.
     */
    @Test
    @DisplayName("Test postUserGroup method (DatabaseConnectionException)")
    void postUserGroupDatabaseConnectionException() {
        doThrow(JpaSystemException.class).when(repository).save(any(UserGroup.class));
        assertThrows(DatabaseConnectionException.class, () -> service.postUserGroup(SAMPLE_USER, SAMPLE_GROUP));
    }

    /**
     * Test for {@link UserGroupsServiceImpl#updateUserGroup} method.
     */
    @Test
    @DisplayName("Test updateUserGroup method (OK)")
    void updateUserGroupOK() {
        // Verify result
        service.updateUserGroup(SAMPLE_USER_GROUP);

        verify(repository, times(1)).save(SAMPLE_USER_GROUP);
    }

    /**
     * Test for {@link UserGroupsServiceImpl#updateUserGroup} method.
     */
    @Test
    @DisplayName("Test updateUserGroup method (DatabaseConnectionException)")
    void updateUserGroupDatabaseConnectionException() {
        when(repository.save(any(UserGroup.class))).thenThrow(JpaSystemException.class);
        assertThrows(DatabaseConnectionException.class, () -> service.updateUserGroup(SAMPLE_USER_GROUP));
    }

    /**
     * Test for {@link UserGroupsServiceImpl#deleteUserGroup} method.
     */
    @Test
    @DisplayName("Test deleteUserGroup method (OK)")
    void deleteUserGroupOK() {
        // Clone the entity so the test doesn't modify the original object
        UserGroup userGroup = UserGroup.builder().id(5L).user(SAMPLE_USER).group(SAMPLE_GROUP).accepted(true).build();

        // Mock repository
        when(repository.findByUser_UsernameAndGroup_Id(any(String.class), any(Long.class)))
                .thenReturn(Optional.of(userGroup));

        // Verify result
        service.deleteUserGroup(SAMPLE_USER.getUsername(), userGroup.getId());

        verify(repository, times(1)).delete(userGroup);
    }

    /**
     * Test for {@link UserGroupsServiceImpl#deleteUserGroup} method.
     */
    @Test
    @DisplayName("Test deleteUserGroup method (DatabaseConnectionException)")
    void deleteUserGroupDatabaseConnectionException() {
        when(repository.findByUser_UsernameAndGroup_Id(any(String.class), any(Long.class))).thenReturn(Optional.of(SAMPLE_USER_GROUP));
        doThrow(JDBCConnectionException.class).when(repository).delete(any(UserGroup.class));
        assertThrows(DatabaseConnectionException.class, () -> service.deleteUserGroup(SAMPLE_USER.getUsername(), SAMPLE_GROUP.getId()));
    }

    /**
     * Test for {@link UserGroupsServiceImpl#findAcceptedUsersInGroup} method.
     */
    @Test
    @DisplayName("Test findAcceptedUsersInGroup method (OK)")
    void findAcceptedUsersInGroupOK() {
        // Mock repository
        when(repository.findByGroup_IdAndAccepted(any(Long.class), any(Boolean.class)))
                .thenReturn(List.of(SAMPLE_USER_GROUP));

        // Verify result
        List<User> result = service.findAcceptedUsersInGroup(SAMPLE_USER_GROUP.getId(), true);

        assertEquals(SAMPLE_USER, result.getFirst());
    }

    /**
     * Test for {@link UserGroupsServiceImpl#findAcceptedGroupsByUser} method.
     */
    @Test
    @DisplayName("Test findAcceptedGroupsByUser method (OK)")
    void findAcceptedGroupsByUserOK() {
        // Mock repository
        when(repository.findByUser_UsernameAndAccepted(any(String.class), any(Boolean.class)))
                .thenReturn(List.of(SAMPLE_USER_GROUP));

        // Verify result
        List<Group> result = service.findAcceptedGroupsByUser(SAMPLE_USER.getUsername(), true);

        assertEquals(1, result.size());
        assertEquals(SAMPLE_GROUP, result.get(0));
    }

    /**
     * Test for {@link UserGroupsServiceImpl#findUserGroup} method.
     */
    @Test
    @DisplayName("Test findUserGroup method (OK)")
    void findUserGroupOK() {
        // Mock repository
        when(repository.findByUser_UsernameAndGroup_Id(any(String.class), any(Long.class))).thenReturn(Optional.of(SAMPLE_USER_GROUP));

        // Verify result
        UserGroup result = service.findUserGroup(SAMPLE_USER.getUsername(), SAMPLE_GROUP.getId());

        assertNotNull(result);
        assertEquals(SAMPLE_USER_GROUP, result);
    }

    /**
     * Test for {@link UserGroupsServiceImpl#findUserGroup} method.
     */
    @Test
    @DisplayName("Test findUserGroup method (EntityNotFoundException)")
    void findUserGroupNotFoundException() {
        when(repository.findByUser_UsernameAndGroup_Id(any(String.class), any(Long.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findUserGroup(SAMPLE_USER.getUsername(), SAMPLE_GROUP.getId()));
    }

    /**
     * Test for {@link UserGroupsServiceImpl#findUserGroup} method.
     */
    @Test
    @DisplayName("Test findUserGroup method (DatabaseConnectionException)")
    void findUserGroupDatabaseConnectionException() {
        doThrow(JDBCConnectionException.class).when(repository).findByUser_UsernameAndGroup_Id(any(String.class), any(Long.class));
        assertThrows(DatabaseConnectionException.class, () -> service.findUserGroup(SAMPLE_USER.getUsername(), SAMPLE_GROUP.getId()));
    }
}
