package quantum.service;

import quantum.dto.auth.AuthResponse;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.model.User;

/**
 * Service for Authentication.
 */
public interface AuthService {

    /**
     * Sign up.
     *
     * @param body The body with the info to sign up.
     * @return The user that signed up.
     */
    AuthResponse signUp(SignUpBody body);

    /**
     * Log in.
     *
     * @param body The body with the info to log in.
     * @return The user that logged in.
     */
    AuthResponse logIn(LogInBody body);
}
