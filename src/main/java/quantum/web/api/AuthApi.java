package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import quantum.model.User;

/**
 * The api interface for {@link User} entity.
 */

@Validated
public interface AuthApi {
    /**
     * POST to /api/auth/login to log in.
     * @return The user that logged in
     */
    @Operation(summary = "Log in service", description = "Log in service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user info that logged in"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/api/auth/login", produces = "application/json")
    ResponseEntity<User> logIn(
        @RequestParam("user")
            String username,
        @RequestParam("password")
            String pwd
    );

}
