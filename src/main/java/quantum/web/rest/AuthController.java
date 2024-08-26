package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.auth.AuthResponse;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
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

    private final AuthService service;

    @Override
    public ResponseEntity<AuthResponse> logIn(LogInBody body) {
        log.info("[CONTROLLER] - Logging in");
        AuthResponse user = service.logIn(body);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<AuthResponse> signUp(SignUpBody body) {
        log.info("[CONTROLLER] - Singing Up");
        AuthResponse user = service.signUp(body);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
