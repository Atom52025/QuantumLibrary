package quantum.service;

import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import quantum.dto.user.UserListResponse;
import quantum.dto.user.UserResponse;
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.UsersMappingImpl;
import quantum.model.User;
import quantum.repository.UserRepository;
import quantum.service.impl.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static quantum.constant.TestConstants.SAMPLE_USER;
import static quantum.constant.TestConstants.SAMPLE_USERNAME;

/**
 * Test for {@link UserServiceImpl} service class.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UsersMappingImpl mapper;

    @InjectMocks
    private UserServiceImpl service;

    private static final NewUserBody SAMPLE_NEW_USER_BODY = new NewUserBody(
            SAMPLE_USER.getUsername(),
            SAMPLE_USER.getEmail(),
            SAMPLE_USER.getPassword()
    );

    private static final UpdateUserBody SAMPLE_UPDATE_USER_BODY = UpdateUserBody.builder()
            .email("newEmail")
            .password("newPassword")
            .username("newUsername")
            .build();

    private static final User SAMPLE_UPDATE_GAME = User.builder()
            .id(1L)
            .username("newUsername")
            .email("newEmail")
            .password("newPassword")
            .role("USER")
            .build();

    /**
     * Test for {@link UserServiceImpl#getUsers} method.
     */
    @Test
    @DisplayName("Test getUsers method (OK)")
    void getUsersOK() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> resultContent = Collections.nCopies(10, SAMPLE_USER);
        Page<User> pagedResult = new PageImpl<>(resultContent, pageable, resultContent.size());

        // Mock repository
        when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);
        when(mapper.map(any(User.class))).thenCallRealMethod();

        // Verify result
        UserListResponse response = service.getUsers(pageable);

        assertEquals(10, response.getUsers().size());
        assertEquals(SAMPLE_USER.getId(), response.getUsers().get(0).getId());
        assertEquals(SAMPLE_USER.getUsername(), response.getUsers().get(0).getUsername());
        assertEquals(SAMPLE_USER.getEmail(), response.getUsers().get(0).getEmail());
        assertEquals(SAMPLE_USER.getRole(), response.getUsers().get(0).getRole());
    }

    /**
     * Test for {@link UserServiceImpl#getUsers} method.
     */
    @Test
    @DisplayName("Test getUsers method (DatabaseConnectionException)")
    void getUsersDatabaseConnectionException() {
        Pageable pageable = PageRequest.of(0, 10);

        // Mock repository
        when(userRepository.findAll(any(Pageable.class))).thenThrow(JDBCConnectionException.class);

        // Verify result
        assertThrows(DatabaseConnectionException.class, () -> service.getUsers(pageable));
    }

    /**
     * Test for {@link UserServiceImpl#getUsers} method.
     */
    @Test
    @DisplayName("Test getUsers method (EntityNotFoundException)")
    void getUsersEntityNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> resultContent = Collections.nCopies(0, SAMPLE_USER);
        Page<User> pagedResult = new PageImpl<>(resultContent, pageable, resultContent.size());

        // Mock repository
        when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

        // Verify result
        assertThrows(EntityNotFoundException.class, () -> service.getUsers(pageable));
    }


    /**
     * Test for {@link UserServiceImpl#loadUserByUsername} method.
     */
    @Test
    @DisplayName("Test loadUserByUsername method (OK)")
    void loadUserByUsernameOK() {

        // Mock repository
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));

        // Verify result
        UserDetails response = service.loadUserByUsername(SAMPLE_USERNAME);

        assertEquals(SAMPLE_USER.getUsername(), response.getUsername());
        assertEquals(SAMPLE_USER.getPassword(), response.getPassword());
    }

    /**
     * Test for {@link UserServiceImpl#loadUserByUsername} method.
     */
    @Test
    @DisplayName("Test loadUserByUsername method (UsernameNotFoundException)")
    void loadUserByUsernameNotFoundException() {

        // Mock repository
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        // Verify result
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(SAMPLE_USERNAME));
    }


    /**
     * Test for {@link UserServiceImpl#postUser} method.
     */
    @Test
    @DisplayName("Test postUser method (OK)")
    void postUserOK() {
        // Mock repository
        when(userRepository.save(any(User.class))).then(element -> element.getArgument(0));
        when(mapper.map(any(User.class))).thenCallRealMethod();

        // Verify result
        UserResponse response = service.postUser(SAMPLE_NEW_USER_BODY);

        assertEquals(SAMPLE_USER.getUsername(), response.getUsername());
        assertEquals(SAMPLE_USER.getEmail(), response.getEmail());
        assertEquals(SAMPLE_USER.getRole(), response.getRole());
    }

    /**
     * Test for {@link UserServiceImpl#postUser} method.
     */
    @Test
    @DisplayName("Test postUser method (Error on save)")
    void postUserDatabaseConnectionException() {
        // Mock repository (DatabaseConnectionException)
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        // Verify result (DatabaseConnectionException)
        assertThrows(DatabaseConnectionException.class, () -> service.postUser(SAMPLE_NEW_USER_BODY));

        // Mock repository (JDBCConnectionException)
        when(userRepository.save(any(User.class))).thenThrow(JDBCConnectionException.class);

        // Verify result (JDBCConnectionException)
        assertThrows(DatabaseConnectionException.class, () -> service.postUser(SAMPLE_NEW_USER_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#updateUser} method.
     */
    @Test
    @DisplayName("Test updateUser method (OK)")
    void updateUserOK() {
        // Clone the game so the test doesn't modify the original object
        User testUser = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), SAMPLE_USER.getPassword(), SAMPLE_USER.getRole());

        // Mock repository
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).then(element -> element.getArgument(0));

        // Verify result
        UserResponse response = service.updateUser(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_USER_BODY);

        assertEquals(SAMPLE_UPDATE_GAME.getId(), response.getId());
        assertEquals(SAMPLE_UPDATE_GAME.getUsername(), response.getUsername());
        assertEquals(SAMPLE_UPDATE_GAME.getEmail(), response.getEmail());
        assertEquals(SAMPLE_UPDATE_GAME.getRole(), response.getRole());
    }

    /**
     * Test for {@link UserServiceImpl#updateUser} method.
     */
    @Test
    @DisplayName("Test updateUser method (DatabaseConnectionException)")
    void updateUserDatabaseConnectionException() {
        // Mock repository
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));
        doThrow(JDBCConnectionException.class).when(userRepository).save(any(User.class));

        // Verify result
        assertThrows(DatabaseConnectionException.class, () -> service.updateUser(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_USER_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#updateUser} method.
     */
    @Test
    @DisplayName("Test updateUser method (EntityNotFoundException)")
    void updateUserEntityNotFoundException() {
        // Mock repository
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        // Verify result
        assertThrows(EntityNotFoundException.class, () -> service.updateUser(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_USER_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#deleteUser} method.
     */
    @Test
    @DisplayName("Test deleteUser method (OK)")
    void deleteUserOK() {
        // Clone the game so the test doesn't modify the original object
        User testUser = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), SAMPLE_USER.getPassword(), SAMPLE_USER.getRole());

        // Mock repository
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));

        // Verify result
        assertDoesNotThrow(() -> service.deleteUser(SAMPLE_USER.getUsername()));
    }

    /**
     * Test for {@link UserServiceImpl#deleteUser} method.
     */
    @Test
    @DisplayName("Test deleteUser method (DatabaseConnectionException)")
    void deleteUserDatabaseConnectionException() {
        // Mock repository
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));
        doThrow(JDBCConnectionException.class).when(userRepository).delete(any(User.class));

        // Verify result
        assertThrows(DatabaseConnectionException.class, () -> service.deleteUser(SAMPLE_USER.getUsername()));
    }
}
