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
@RestController
@Slf4j
public class SteamGridDBApi {
    private final String externalApiUrl = "https://www.steamgriddb.com/api/v2/search/autocomplete/";
    private final String authToken = "1a8c79dc9eff0c11128e1a230c8abdae";
    private final WebClient webClient;

    public SteamGridDBApi(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(externalApiUrl).build();
    }

    /**
     * GET to /api/auth/login to log in.
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
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<String> fetchData(@RequestParam(value = "term", required = false) String term) {
        String apiUrl = externalApiUrl + term;
        Mono<String> result = webClient.get()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .retrieve()
                .bodyToMono(String.class);
        log.info(result.block());
        return ResponseEntity.ok(result.block());
    }

};



