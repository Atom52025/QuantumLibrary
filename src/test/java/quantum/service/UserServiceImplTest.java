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
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import quantum.dto.user.*;
import quantum.exceptions.BadPasswordException;
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
class UserServiceImplTest {

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
            .image("image")
            .build();

    private static final UpdatePasswordBody SAMPLE_UPDATE_PASSWORD_BODY = UpdatePasswordBody.builder()
            .oldPassword(SAMPLE_USER.getPassword())
            .newPassword("newPassword")
            .build();

    private static final User SAMPLE_UPDATE_USER = User.builder()
            .id(1L)
            .username("newUsername")
            .email("newEmail")
            .password("newPassword")
            .role("USER")
            .image("image")
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

        // Mock dependencies
        when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);
        when(mapper.map(any(User.class))).thenCallRealMethod();

        // Verify result
        UserListResponse response = service.getUsers(pageable);

        assertEquals(10, response.getUsers().size());
        assertEquals(SAMPLE_USER.getUsername(), response.getUsers().getFirst().getUsername());
        assertEquals(SAMPLE_USER.getEmail(), response.getUsers().getFirst().getEmail());
        assertEquals(SAMPLE_USER.getRole(), response.getUsers().getFirst().getRole());
    }

    /**
     * Test for {@link UserServiceImpl#getUsers} method.
     */
    @Test
    @DisplayName("Test getUsers method (DatabaseConnectionException)")
    void getUsersDatabaseConnectionException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(any(Pageable.class))).thenThrow(JDBCConnectionException.class);
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
        when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);
        assertThrows(EntityNotFoundException.class, () -> service.getUsers(pageable));
    }

    /**
     * Test for {@link UserServiceImpl#getUser} method.
     */
    @Test
    @DisplayName("Test getUser method (OK)")
    void getUserOK() {
        // Mock dependencies
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));

        // Verify result
        UserResponse response = service.getUser(SAMPLE_USER.getUsername());

        assertEquals(SAMPLE_USER.getUsername(), response.getUsername());
        assertEquals(SAMPLE_USER.getEmail(), response.getEmail());
        assertEquals(SAMPLE_USER.getRole(), response.getRole());
    }

    /**
     * Test for {@link UserServiceImpl#getUser} method.
     */
    @Test
    @DisplayName("Test getUser method (EntityNotFoundException)")
    void getUserNotFoundException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getUser(SAMPLE_USERNAME));
    }

    /**
     * Test for {@link UserServiceImpl#getUser} method.
     */
    @Test
    @DisplayName("Test getUser method (DatabaseConnectionException)")
    void getUserDatabaseConnectionException() {
        doThrow(JDBCConnectionException.class).when(userRepository).findByUsername(any(String.class));
        assertThrows(DatabaseConnectionException.class, () -> service.getUser(SAMPLE_USERNAME));
    }

    /**
     * Test for {@link UserServiceImpl#getUser} method.
     */
    @Test
    @DisplayName("Test findUser method (OK)")
    void findUserOK() {
        // Mock dependencies
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));

        // Verify result
        User response = service.findUser(SAMPLE_USER.getUsername());

        assertEquals(SAMPLE_USER.getUsername(), response.getUsername());
        assertEquals(SAMPLE_USER.getEmail(), response.getEmail());
        assertEquals(SAMPLE_USER.getRole(), response.getRole());
    }

    /**
     * Test for {@link UserServiceImpl#getUser} method.
     */
    @Test
    @DisplayName("Test findUser method (DatabaseConnectionException)")
    void findUserDatabaseConnectionException() {
        doThrow(JDBCConnectionException.class).when(userRepository).findByUsername(any(String.class));
        assertThrows(DatabaseConnectionException.class, () -> service.findUser(SAMPLE_USERNAME));
    }

    /**
     * Test for {@link UserServiceImpl#loadUserByUsername} method.
     */
    @Test
    @DisplayName("Test loadUserByUsername method (OK)")
    void loadUserByUsernameOK() {

        // Mock dependencies
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
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(SAMPLE_USERNAME));
    }


    /**
     * Test for {@link UserServiceImpl#postUser} method.
     */
    @Test
    @DisplayName("Test postUser method (OK)")
    void postUserOK() {
        // Mock dependencies
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
    @DisplayName("Test postUser method (DataIntegrityViolationException)")
    void postUserDataIntegrityViolationException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));
        assertThrows(DataIntegrityViolationException.class, () -> service.postUser(SAMPLE_NEW_USER_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#postUser} method.
     */
    @Test
    @DisplayName("Test postUser method (DatabaseConnectionException)")
    void postUserDatabaseConnectionException() {
        // DataIntegrityViolationException
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(DatabaseConnectionException.class, () -> service.postUser(SAMPLE_NEW_USER_BODY));
        // JDBCConnectionException
        when(userRepository.save(any(User.class))).thenThrow(JDBCConnectionException.class);
        assertThrows(DatabaseConnectionException.class, () -> service.postUser(SAMPLE_NEW_USER_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#checkUser} method.
     */
    @Test
    @DisplayName("Test checkUser method (DatabaseConnectionException)")
    void checkUserDatabaseConnectionException() {
        when(userRepository.findByUsername(any(String.class))).thenThrow(JpaSystemException.class);
        assertThrows(DatabaseConnectionException.class, () -> service.postUser(SAMPLE_NEW_USER_BODY));
    }


    /**
     * Test for {@link UserServiceImpl#updateUser} method.
     */
    @Test
    @DisplayName("Test updateUser method (OK)")
    void updateUserOK() {
        // Clone the entity so the test doesn't modify the original object
        User testUser = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), SAMPLE_USER.getPassword(), SAMPLE_USER.getRole(), SAMPLE_USER.getImage(), SAMPLE_USER.getUserGames(), SAMPLE_USER.getUserGroups());

        // Mock dependencies
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).then(element -> element.getArgument(0));

        // Verify result
        UserResponse response = service.updateUser(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_USER_BODY);

        assertEquals(SAMPLE_UPDATE_USER.getImage(), response.getImage());
        assertEquals(SAMPLE_UPDATE_USER.getEmail(), response.getEmail());
    }

    /**
     * Test for {@link UserServiceImpl#updateUser} method.
     */
    @Test
    @DisplayName("Test updateUser method (DatabaseConnectionException)")
    void updateUserDatabaseConnectionException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));
        doThrow(JDBCConnectionException.class).when(userRepository).save(any(User.class));
        assertThrows(DatabaseConnectionException.class, () -> service.updateUser(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_USER_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#updateUser} method.
     */
    @Test
    @DisplayName("Test updateUser method (EntityNotFoundException)")
    void updateUserEntityNotFoundException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.updateUser(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_USER_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#updatePassword} method.
     */
    @Test
    @DisplayName("Test updatePassword method (OK)")
    void updatePasswordOK() {
        // Clone the entity so the test doesn't modify the original object
        User testUser = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), BCrypt.hashpw(SAMPLE_USER.getPassword(), BCrypt.gensalt()), SAMPLE_USER.getRole(), SAMPLE_USER.getImage(), SAMPLE_USER.getUserGames(), SAMPLE_USER.getUserGroups());

        // Mock dependencies
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).then(element -> element.getArgument(0));

        // Verify result
        UserResponse response = service.updatePassword(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_PASSWORD_BODY);

        assertNotNull(response);
    }

    /**
     * Test for {@link UserServiceImpl#updatePassword} method.
     */
    @Test
    @DisplayName("Test updatePassword method (DatabaseConnectionException)")
    void updatePasswordDatabaseConnectionException() {
        User testUser = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), BCrypt.hashpw(SAMPLE_USER.getPassword(), BCrypt.gensalt()), SAMPLE_USER.getRole(), SAMPLE_USER.getImage(), SAMPLE_USER.getUserGames(), SAMPLE_USER.getUserGroups());
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));
        doThrow(JpaSystemException.class).when(userRepository).save(any(User.class));
        assertThrows(DatabaseConnectionException.class, () -> service.updatePassword(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_PASSWORD_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#updatePassword} method.
     */
    @Test
    @DisplayName("Test updatePassword method (BadPasswordException)")
    void updatePasswordBadPasswordException() {
        User testUser = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), BCrypt.hashpw("incorrectPassword", BCrypt.gensalt()), SAMPLE_USER.getRole(), SAMPLE_USER.getImage(), SAMPLE_USER.getUserGames(), SAMPLE_USER.getUserGroups());
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));
        assertThrows(BadPasswordException.class, () -> service.updatePassword(SAMPLE_USER.getUsername(), SAMPLE_UPDATE_PASSWORD_BODY));
    }

    /**
     * Test for {@link UserServiceImpl#deleteUser} method.
     */
    @Test
    @DisplayName("Test deleteUser method (OK)")
    void deleteUserOK() {
        // Clone the entity so the test doesn't modify the original object
        User testUser = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), SAMPLE_USER.getPassword(), SAMPLE_USER.getRole(), SAMPLE_USER.getImage(), SAMPLE_USER.getUserGames(), SAMPLE_USER.getUserGroups());

        // Mock dependencies
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
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));
        doThrow(JDBCConnectionException.class).when(userRepository).delete(any(User.class));
        assertThrows(DatabaseConnectionException.class, () -> service.deleteUser(SAMPLE_USER.getUsername()));
    }
}
