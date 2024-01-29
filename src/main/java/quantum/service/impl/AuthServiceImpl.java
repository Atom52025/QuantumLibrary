package quantum.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.dto.user.DataListResponseUser;
import quantum.dto.user.DataResponseUser;
import quantum.dto.user.NewUserBody;
import quantum.dto.user.UpdateUserBody;
import quantum.exceptions.DatabaseConnectionException;
import quantum.mapping.UsersMapping;
import quantum.model.User;
import quantum.repository.UserRepository;
import quantum.service.AuthService;
import quantum.service.UserService;
import quantum.web.rest.AuthController;

import java.util.Optional;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;
import static quantum.constant.ErrorConstants.ENTITY_NOT_FOUND_ERROR;

/**
 * Service implementation for {@link User} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UsersMapping mapper;

    /**
     * Sign up.
     * @param body The body with the info to sign up.
     * @return The user that signed up.
     */
    @Override
    public DataResponseUser signUp(SignUpBody body) {
        Optional<User> user = userRepository.findByUsername(body.getUsername());
        if (user.isEmpty()) {
            User newUser = generateNewUser(body);
            return mapper.map(userRepository.save(newUser));
        } else {
            return null;
        }
    }

    /**
     * Log in.
     * @param body The body with the info to log in.
     * @return The user that logged in.
     */
    @Override
    public DataResponseUser logIn(LogInBody body) {
        Optional<User> user = userRepository.findByUsername(body.getUsername());
        if (user.isEmpty()) {
            return null;
        }
        if (BCrypt.checkpw(body.getPassword(), user.get().getPassword())){
            return mapper.map(user.get());
        } else {
            return null;
        }
    }

    /**
     * Generate a new user.
     * @param body the body
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
