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
import quantum.dto.userGames.NewUserGameBody;
import quantum.dto.userGames.UpdateUserGameBody;
import quantum.dto.userGames.UserGameResponse;
import quantum.dto.userGames.UserGamesListResponse;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.model.UserGame;

/**
 * The api interface for {@link UserGame} entity.
 */

@Validated
public interface UserGamesApi {

    /**
     * GET to /api/user/{username}/games to fetch a user game list.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @param category The category.
     * @param pageable The pageable.
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
            @Parameter(in = ParameterIn.PATH, required = true, description = "The username")
            @PathVariable("username")
            String username,
            @RequestParam(value = "category", required = false, defaultValue = "all")
            String category,
            @PageableDefault(value = Integer.MAX_VALUE)
            Pageable pageable
    );

    /**
     * POST to /api/user/{username}/games/{game_sgdb_id} add a game to a user
     *
     * @param token      The token with the authentication information.
     * @param username   The username.
     * @param gameSgdbId The SGDB game id.
     * @param body       The user game body.
     * @return The new user game.
     */
    @Operation(summary = "Create user game", description = "Create user game", parameters = {
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
    @PostMapping(value = "/api/user/{username}/games/{game_sgdb_id}", produces = "application/json")
    ResponseEntity<UserGameResponse> postUserGame(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The username")
            @PathVariable("username")
            String username,
            @PathVariable("game_sgdb_id")
            Long gameSgdbId,
            @Valid @RequestBody
            NewUserGameBody body
    );

    /**
     * POST to /api/user/{username}/games/import to import a list of games to a user.
     *
     * @param token    The token with the authentication information.
     * @param username The username.
     * @param body     Import list body.
     * @return The new user games.
     */
    @Operation(summary = "Import user games", description = "Import user games", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size"),
            @Parameter(name = "sort", description = "The sort order")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of games imported"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/api/user/{username}/games/import", produces = "application/json")
    ResponseEntity<UserGamesListResponse> importUserGames(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The username")
            @PathVariable("username")
            String username,
            @Valid @RequestBody
            UserGamesImportList body
    );

    /**
     * PATCH to /api/user/{username}/games/{game_sgdb_id} to edit a game from a user.
     *
     * @param token      The token with the authentication information.
     * @param username   The username.
     * @param gameSgdbId The game id.
     * @param body       The user game body.
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
    @PatchMapping(value = "/api/user/{username}/games/{game_sgdb_id}", produces = "application/json")
    ResponseEntity<UserGameResponse> patchUserGame(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The username")
            @PathVariable("username")
            String username,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The sgdb game id")
            @PathVariable("game_sgdb_id")
            Long gameSgdbId,
            @Valid @RequestBody
            UpdateUserGameBody body
    );

    /**
     * DELETE to /api/user/{username}/games/{game_sgdb_id} to delete a game from a user.
     *
     * @param token      The token with the authentication information.
     * @param username   The username.
     * @param gameSgdbId The game id.
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
    @DeleteMapping(value = "/api/user/{username}/games/{game_sgdb_id}", produces = "application/json")
    ResponseEntity<Void> deleteUserGame(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The username")
            @PathVariable("username")
            String username,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The sgdb game id")
            @PathVariable("game_sgdb_id")
            Long gameSgdbId
    );
}
