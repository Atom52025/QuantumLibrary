package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.dto.user.DataResponseUser;
import quantum.model.User;
import quantum.service.AuthService;
import quantum.web.api.AuthApi;

/**
 * Controller for {@link User} entity.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private  final AuthService authService;

    @Override
    public ResponseEntity<DataResponseUser> logIn(LogInBody body) {
        log.info("[CONTROLLER] - Logging in");
        DataResponseUser user = authService.logIn(body);
        log.info("[CONTROLLER RESULT] - The user with id '{}' was deleted", user.getId());
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<DataResponseUser> signUp(SignUpBody body) {
        log.info("[CONTROLLER] - Singing Up");
        DataResponseUser user = authService.signUp(body);
        log.info("[CONTROLLER RESULT] - The user with id '{}' was deleted", user.getId());
        return ResponseEntity.ok(user);
    }
}
