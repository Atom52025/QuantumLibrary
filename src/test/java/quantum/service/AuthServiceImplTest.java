package quantum.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import quantum.dto.auth.AuthResponse;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.mapping.UsersMappingImpl;
import quantum.model.User;
import quantum.repository.UserRepository;
import quantum.service.impl.AuthServiceImpl;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static quantum.constant.TestConstants.SAMPLE_USER;

/**
 * Test for {@link AuthServiceImpl} class.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Spy
    private UsersMappingImpl mapper;

    @InjectMocks
    private AuthServiceImpl service;

    static final SignUpBody SAMPLE_SIGN_UP_BODY = SignUpBody.builder()
            .username(SAMPLE_USER.getUsername())
            .password(SAMPLE_USER.getPassword())
            .email(SAMPLE_USER.getEmail())
            .build();

    static final LogInBody SAMPLE_LOG_IN_BODY = LogInBody.builder()
            .username(SAMPLE_USER.getUsername())
            .password(SAMPLE_USER.getPassword())
            .build();

    static final AuthResponse SAMPLE_AUTH_RESPONSE = AuthResponse.builder()
            .id(1L)
            .username(SAMPLE_USER.getUsername())
            .email(SAMPLE_USER.getEmail())
            .image(SAMPLE_USER.getImage())
            .role(SAMPLE_USER.getRole())
            .token("jwtToken")
            .build();

    /**
     * Test for {@link AuthServiceImpl#signUp} method.
     */
    @Test
    @DisplayName("Test signUp method (OK)")
    void signUpOK() {
        AtomicReference<User> savedUser = new AtomicReference<>();

        // Mock dependencies
        when(mapper.authMap(any(User.class))).thenCallRealMethod();
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(any(String.class)))
                // First call: user not found
                .thenReturn(Optional.empty())
                // Second call: return saved user
                .thenAnswer(invocation -> Optional.ofNullable(savedUser.get()));

        // Extract the user from the invocation and save it
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            savedUser.set(user);
            return user;
        }).when(userRepository).save(any(User.class));

        // Verify result
        AuthResponse result = service.signUp(SAMPLE_SIGN_UP_BODY);

        assertNotNull(result);
        assertEquals(SAMPLE_AUTH_RESPONSE.getUsername(), result.getUsername());
    }

    /**
     * Test for {@link AuthServiceImpl#signUp} method.
     */
    @Test
    @DisplayName("Test signUp method (EntityFoundException)")
    void signUpEntityFoundException() {
        // Mock dependencies
        when(userRepository.findByUsername(SAMPLE_SIGN_UP_BODY.getUsername())).thenReturn(Optional.of(SAMPLE_USER));

        // Verify result
        AuthResponse result = service.signUp(SAMPLE_SIGN_UP_BODY);

        assertNull(result);
    }

    /**
     * Test for {@link AuthServiceImpl#logIn} method.
     */
    @Test
    @DisplayName("Test logIn method (OK)")
    void logInOK() {
        // Mock dependencies
        when(mapper.authMap(any(User.class))).thenCallRealMethod();
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(SAMPLE_USER));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        // Verify result
        AuthResponse result = service.logIn(SAMPLE_LOG_IN_BODY);

        assertNotNull(result);
        assertEquals(SAMPLE_AUTH_RESPONSE.getUsername(), result.getUsername());
    }

    /**
     * Test for {@link AuthServiceImpl#logIn} method.
     */
    @Test
    @DisplayName("Test logIn method (EntityNotFoundException)")
    void logInEntityNotFoundException() {
        // Mock dependencies
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        // Verify result
        AuthResponse result = service.logIn(SAMPLE_LOG_IN_BODY);

        assertNull(result);
    }
}
