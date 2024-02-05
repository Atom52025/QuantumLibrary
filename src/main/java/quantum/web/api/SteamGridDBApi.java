package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.dto.auth.AuthResponse;
import quantum.dto.auth.LogInBody;
import quantum.dto.auth.SignUpBody;
import quantum.model.User;
import reactor.core.publisher.Mono;

/**
 * The api interface for {@link User} entity.
 */

@Validated
@RequestMapping("/api/sgdb")
public interface SteamGridDBApi {

    /**
     * GET to /api/sgbd/search to search in sgbd by term.
     * @param term The term to search for
     * @return The games founded.
     */
    @Operation(summary = "Search in SGDB for a name by its name", description = "Search in SGDB for a name by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The games founded"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/search", produces = "application/json")
    ResponseEntity<String> searchByTerm(
            @RequestParam(value = "term", required = false)
                String term);

};



