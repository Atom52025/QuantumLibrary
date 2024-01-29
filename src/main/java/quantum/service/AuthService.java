package quantum.service;

import org.springframework.data.domain.Pageable;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.dto.game.DataListResponseGame;
import quantum.dto.game.DataResponseGame;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.dto.user.DataResponseUser;
import quantum.model.Game;

/**
 * Service for Authentication.
 */
public interface AuthService {

        /**
        * Sign up.
        * @param body The body with the info to sign up.
        * @return The user that signed up.
        */
        DataResponseUser signUp(SignUpBody body);

        /**
         * Log in.
         * @param body The body with the info to log in.
         * @return The user that logged in.
         */
        DataResponseUser logIn(LogInBody body);


}
