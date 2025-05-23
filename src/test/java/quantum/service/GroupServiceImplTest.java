package quantum.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import quantum.dto.group.*;
import quantum.dto.userGroups.UserGroupsListResponse;
import quantum.exceptions.BadRequestException;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityFoundException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.GamesMappingImpl;
import quantum.mapping.GroupMappingImpl;
import quantum.model.Group;
import quantum.model.User;
import quantum.model.UserGroup;
import quantum.repository.GroupRepository;
import quantum.service.impl.GroupServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quantum.constant.TestConstants.*;

/**
 * Test for {@link GroupServiceImpl} service class.
 */
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository repository;

    @Mock
    private UserGroupsService userGroupsService;

    @Mock
    private UserGamesService userGamesService;

    @Mock
    private UserService userService;

    @Spy
    private GroupMappingImpl mapper;

    @Spy
    private GamesMappingImpl gamesMapping;

    @InjectMocks
    private GroupServiceImpl service;

    private static final NewGroupBody SAMPLE_NEW_GROUP_BODY = new NewGroupBody("Sample Group", Collections.singletonList("invitedUser1"));
    private static final UpdateGroupBody SAMPLE_UPDATE_GROUP_BODY = new UpdateGroupBody("Updated Group Name");

    @Test
    @DisplayName("Test getGroupGames method (OK)")
    void getGroupGamesOK() {
        // Mock dependencies
        when(userGroupsService.findAcceptedUsersInGroup(any(Long.class), any(Boolean.class))).thenReturn(Collections.singletonList(SAMPLE_USER));
        when(userGamesService.getCommonGames(any(List.class))).thenReturn(Set.of(SAMPLE_GAME));
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(SAMPLE_GROUP));

        // Verify result
        GroupGamesResponse response = service.getGroupGames(1L);

        assertEquals(SAMPLE_GROUP.getName(), response.getGroup().getName());
        assertEquals(1, response.getGames().size());
        assertEquals(SAMPLE_GAME.getName(), response.getGames().getFirst().getName());
    }

    @Test
    @DisplayName("Test getGroupGames method (EntityNotFoundException)")
    void getGroupGamesEntityNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getGroupGames(1L));
    }

    @Test
    @DisplayName("Test getUserGroups method (OK)")
    void getUserGroupsOK() {
        // Mock dependencies
        when(userGroupsService.findAcceptedGroupsByUser("testUser", true)).thenReturn(Collections.emptyList());
        when(userGroupsService.findAcceptedGroupsByUser("testUser", false)).thenReturn(Collections.emptyList());

        // Verify result
        UserGroupsListResponse response = service.getUserGroups("testUser");

        assertTrue(response.getAccepted().isEmpty());
        assertTrue(response.getPending().isEmpty());
    }

    @Test
    @DisplayName("Test sendInvite method (OK)")
    void sendInviteOK() {
        Group newGroup = new Group(1L, "Sample Group", new ArrayList<>());

        // Mock dependencies
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(newGroup));
        when(userService.findUser(any(String.class))).thenReturn(SAMPLE_USER);
        when(userGroupsService.postUserGroup(any(User.class), any(Group.class))).thenReturn(new UserGroup());

        // Verify result
        UserGroup response = service.sendInvite("invitedUser", 1L);

        verify(userGroupsService).postUserGroup(SAMPLE_USER, newGroup);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Test sendInvite method (EntityFoundException)")
    void sendInviteEntityFoundException() {
        Group group = SAMPLE_GROUP;
        group.getUserGroups().add(SAMPLE_USER_GROUP);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(group));
        when(userService.findUser(any(String.class))).thenReturn(SAMPLE_USER);
        assertThrows(EntityFoundException.class, () -> service.sendInvite("testUser", 1L));
    }

    @Test
    @DisplayName("Test joinGroup method (OK)")
    void joinGroupOK() {
        // Mock dependencies
        when(userGroupsService.findUserGroup(any(String.class), any(Long.class))).thenReturn(SAMPLE_USER_GROUP);
        doNothing().when(userGroupsService).updateUserGroup(SAMPLE_USER_GROUP);

        // Verify result
        service.joinGroup("testUser", 1L);

        verify(userGroupsService).updateUserGroup(SAMPLE_USER_GROUP);
    }

    @Test
    @DisplayName("Test declineOrExitGroup method (OK)")
    void declineOrExitGroupOK() {
        // Clone the group so the test doesn't modify the original object
        Group testGroup = new Group(SAMPLE_GROUP.getId(), SAMPLE_GROUP.getName(), new ArrayList<>());
        testGroup.getUserGroups().add(SAMPLE_USER_GROUP);

        // Mock dependencies
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(testGroup));

        // Verify result
        service.declineOrExitGroup(SAMPLE_USER.getUsername(), SAMPLE_GROUP.getId());

        assertTrue(testGroup.getUserGroups().isEmpty());
        verify(repository).save(testGroup);
    }

    @Test
    @DisplayName("Test declineOrExitGroup method (NotFoundException)")
    void declineOrExitGroupNotFoundException() {
        // Clone the group so the test doesn't modify the original object
        Group testGroup = new Group(SAMPLE_GROUP.getId(), SAMPLE_GROUP.getName(), new ArrayList<>());

        // Don't add the user so the group is empty
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(testGroup));

        assertThrows(EntityNotFoundException.class, () -> service.declineOrExitGroup(SAMPLE_USER.getUsername(), SAMPLE_GROUP.getId()));
    }

    @Test
    @DisplayName("Test getGroups method (OK)")
    void getGroupsOK() {
        // Mock dependencies
        when(repository.findAll()).thenReturn(List.of(SAMPLE_GROUP));

        // Verify result
        GroupListResponse response = service.getGroups();

        assertEquals(1, response.getGroups().size());
    }

    @Test
    @DisplayName("Test getGroups method (DatabaseConnectionException)")
    void getGroupsEntityDatabaseConnectionException() {
        doThrow(JpaSystemException.class).when(repository).findAll();
        assertThrows(DatabaseConnectionException.class, () -> service.getGroups());
    }

    @Test
    @DisplayName("Test getGroups method (EntityNotFoundException)")
    void getGroupsEntityNotFoundException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> service.getGroups());
    }

    @Test
    @DisplayName("Test postGroup method (OK)")
    void postGroupOK() {
        // Mock dependencies
        when(repository.save(any(Group.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Verify result
        GroupResponse response = service.postGroup("testUser", SAMPLE_NEW_GROUP_BODY);

        verify(repository).save(any(Group.class));
        assertNotNull(response);
    }

    @Test
    @DisplayName("Test postGroup method (BadRequestException)")
    void postGroupBadRequestException() {
        NewGroupBody body = new NewGroupBody("", Collections.emptyList());
        assertThrows(BadRequestException.class, () -> service.postGroup("testUser", body));
    }

    @Test
    @DisplayName("Test postGroup method (DatabaseConnectionException)")
    void postGroupDatabaseConnectionException() {
        doThrow(JpaSystemException.class).when(repository).save(any(Group.class));
        assertThrows(DatabaseConnectionException.class, () -> service.postGroup("testUser", SAMPLE_NEW_GROUP_BODY));
    }

    @Test
    @DisplayName("Test postGroup method (DataIntegrityViolationException)")
    void postGroupDataIntegrityViolationException() {
        doThrow(DataIntegrityViolationException.class).when(repository).save(any(Group.class));
        assertThrows(DatabaseConnectionException.class, () -> service.postGroup("testUser", SAMPLE_NEW_GROUP_BODY));
    }

    @Test
    @DisplayName("Test updateGroup method (OK)")
    void updateGroupOK() {
        // Clone the group so the test doesn't modify the original object
        Group testGroup = new Group(SAMPLE_GROUP.getId(), SAMPLE_GROUP.getName(), SAMPLE_GROUP.getUserGroups());

        // Mock dependencies
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(testGroup));
        when(repository.save(any(Group.class))).then(element -> element.getArgument(0));

        // Verify result
        GroupResponse response = service.updateGroup(SAMPLE_GROUP.getId(), SAMPLE_UPDATE_GROUP_BODY);

        assertEquals("Updated Group Name", response.getName());
    }

    @Test
    @DisplayName("Test updateGroup method (EntityNotFoundException)")
    void updateGroupEntityNotFoundException() {
        doThrow(JpaSystemException.class).when(repository).findById(any(Long.class));
        assertThrows(DatabaseConnectionException.class, () -> service.updateGroup(1L, SAMPLE_UPDATE_GROUP_BODY));
    }

    @Test
    @DisplayName("Test updateGroup method (DatabaseConnectionException)")
    void updateGroupDatabaseConnectionException() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(SAMPLE_GROUP));
        doThrow(JpaSystemException.class).when(repository).save(any(Group.class));
        assertThrows(DatabaseConnectionException.class, () -> service.updateGroup(1L, SAMPLE_UPDATE_GROUP_BODY));
    }

    @Test
    @DisplayName("Test deleteGroup method (OK)")
    void deleteGroupOK() {
        // Clone the group so the test doesn't modify the original object
        Group testGroup = new Group(SAMPLE_GROUP.getId(), SAMPLE_GROUP.getName(), SAMPLE_GROUP.getUserGroups());

        // Mock dependencies
        doNothing().when(repository).delete(any(Group.class));

        // Verify result
        assertDoesNotThrow(() -> service.deleteGroup(testGroup));
    }

    @Test
    @DisplayName("Test deleteGroup method (DatabaseConnectionException)")
    void deleteGroupDatabaseConnectionException() {
        doThrow(JpaSystemException.class).when(repository).delete(any(Group.class));
        assertThrows(DatabaseConnectionException.class, () -> service.deleteGroup(SAMPLE_GROUP));
    }

    @Test
    @DisplayName("Test voteGroupGame method (OK)")
    void voteGroupGameOK() {
        // Mock dependencies
        when(userGroupsService.findUserGroup(any(String.class), any(Long.class))).thenReturn(SAMPLE_USER_GROUP);

        // Mock service
        doNothing().when(userGroupsService).updateUserGroup(any(UserGroup.class));

        // Verify result
        assertDoesNotThrow(() -> service.voteGroupGame(SAMPLE_USER_GROUP.getUser().getUsername(), SAMPLE_USER_GROUP.getGroup().getId(), 1L));
    }

    @Test
    @DisplayName("Test voteGroupGame method (DatabaseConnectionException)")
    void voteGroupGameDatabaseConnectionException() {
        UserGroup userGroup = new UserGroup(SAMPLE_USER_GROUP.getId(), SAMPLE_USER_GROUP.getUser(), SAMPLE_USER_GROUP.getGroup(), true, List.of(1L));
        when(userGroupsService.findUserGroup(any(String.class), any(Long.class))).thenReturn(userGroup);
        doThrow(JpaSystemException.class).when(userGroupsService).updateUserGroup(any(UserGroup.class));
        assertThrows(DatabaseConnectionException.class, () -> service.voteGroupGame(SAMPLE_USER_GROUP.getUser().getUsername(), SAMPLE_USER_GROUP.getGroup().getId(), 1L));
    }

}
