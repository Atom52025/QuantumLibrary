package quantum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import quantum.dto.auth.AuthResponse;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.mapping.UsersMapping;
import quantum.model.User;
import quantum.repository.UserRepository;
import quantum.security.jwt.JwtUtil;
import quantum.service.AuthService;

import java.util.Optional;

/**
 * Service implementation for {@link User} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UsersMapping mapper;

    //------------------------------------- PUBLIC METHODS -------------------------------------//

    /**
     * Sign up.
     *
     * @param body The body with the info to sign up.
     * @return The user that signed up.
     */
    @Override
    public AuthResponse signUp(SignUpBody body) {
        Optional<User> user = userRepository.findByUsername(body.getUsername());
        if (user.isEmpty()) {
            User newUser = generateNewUser(body);
            userRepository.save(newUser);
            return logIn(new LogInBody(body.getUsername(), body.getPassword()));
        } else {
            log.info("User already exists");
            return null;
        }
    }

    /**
     * Log in.
     *
     * @param body The body with the info to log in.
     * @return The user that logged in.
     */
    @Override
    public AuthResponse logIn(LogInBody body) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));
        Optional<User> user = userRepository.findByUsername(body.getUsername());
        if (user.isEmpty()) {
            log.info("User doesnt exist");
            return null;
        }
        AuthResponse response = mapper.authMap(user.get());
        response.setToken(JwtUtil.generateJwtToken(user.get()));
        return response;
    }

    //------------------------------------- PRIVATE METHODS -------------------------------------//

    /**
     * Generate a new user.
     *
     * @param body The body
     * @return the user
     */
    private User generateNewUser(SignUpBody body) {
        return User.builder()
                .username(body.getUsername())
                .email(body.getEmail())
                .password(BCrypt.hashpw(body.getPassword(), BCrypt.gensalt()))
                .build();
    }
}
