package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import quantum.dto.auth.AuthResponse;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.model.User;

/**
 * The api interface for {@link User} entity.
 */

@Validated
@RequestMapping("/api/auth")
public interface AuthApi {
    /**
     * POST to /api/auth/login to log in.
     *
     * @return The user that logged in
     */
    @Operation(summary = "Log in service", description = "Log in service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user info that logged in"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/login", produces = "application/json")
    ResponseEntity<AuthResponse> logIn(
            @Valid @RequestBody
            LogInBody body
    );

    /**
     * POST to /api/auth/login to sign up.
     *
     * @return The user that signed up
     */
    @Operation(summary = "Sing up service", description = "Sing up service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user info that signed up"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/signup", produces = "application/json")
    ResponseEntity<AuthResponse> signUp(
            @Valid @RequestBody
            SignUpBody body
    );

}
