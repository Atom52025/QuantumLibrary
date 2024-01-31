package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quantum.dto.usergames.NewUserGameBody;
import quantum.dto.usergames.UpdateUserGameBody;
import quantum.dto.usergames.UserGamesListResponse;
import quantum.dto.usergames.UserGameResponse;
import quantum.model.UserGame;

/**
 * The api interface for {@link UserGame} entity.
 */

@Validated
public interface UserGamesApi {

    /**
     * GET to /api/user/{username}/games to fetch a user game list.
     * @param token The token with the authentication information.
     * @param username The username.
     * @param pageable the pageable
     * @return The list of games
     */
    @Operation(summary = "Get games list", description = "Get games list", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of games"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/api/user/{username}/games", produces = "application/json")
    ResponseEntity<UserGamesListResponse> getUserGames(
            @RequestHeader("Authorization")
                String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user game id")
            @PathVariable("username")
                String username,
            @PageableDefault
                Pageable pageable
    );

    /**
     * POST to /api/user/{username}/games add a game to a user
     * @param token The token with the authentication information.
     * @param username The username.
     * @param gameId The game id.
     * @param body The user game body.
     * @return The new user game.
     */
    @Operation(summary = "Get games list", description = "Get games list", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of games"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/api/user/{username}/games/{game_id}", produces = "application/json")
    ResponseEntity<UserGameResponse> postUserGame(
            @RequestHeader("Authorization")
                String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user game id")
            @PathVariable("username")
                String username,
            @PathVariable("game_id")
                Long gameId,
            @Valid @RequestBody
                NewUserGameBody body
    );

    /**
     * PATCH to /api/user/{username}/games/{game_id} to edit a game from a user.
     * @param token The token with the authentication information.
     * @param username The username.
     * @param gameId The game id.
     * @param body The user game body.
     * @return The edited user game.
     */
    @Operation(summary = "Edit a user game", description = "Edit a user game", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of games"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/api/user/{username}/games/{game_id}", produces = "application/json")
    ResponseEntity<UserGameResponse> patchUserGame(
            @RequestHeader("Authorization")
                String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user game id")
            @PathVariable("username")
                String username,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user game id")
            @PathVariable("game_id")
                Long gameId,
            @Valid @RequestBody
                UpdateUserGameBody body
    );

    /**
     * DELETE to /api/user/{username}/games/{game_id} to delete a game from a user.
     * @param token The token with the authentication information.
     * @param username The username.
     * @param gameId The game id.
     */
    @Operation(summary = "Delete a user game", description = "Delete a user game", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/api/user/{username}/games/{game_id}", produces = "application/json")
    ResponseEntity<Void> deleteUserGame(
            @RequestHeader("Authorization")
                String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user game id")
            @PathVariable("username")
                String username,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The user game id")
            @PathVariable("game_id")
                Long gameId
    );
}
