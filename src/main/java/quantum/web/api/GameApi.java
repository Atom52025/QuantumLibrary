package quantum.web.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.DataResponseGame;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.model.Game;

/**
 * The api interface for {@link Game} entity.
 */

@Validated
public interface GameApi {

    /**
     * GET to /api/games to fetch games list.
     * @param token The token with the authentication information.
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
    @GetMapping(value = "/api/games", produces = "application/json")
    ResponseEntity<GameListResponse> getGames(
            @RequestHeader("Authorization")
                String token,
            @PageableDefault
                Pageable pageable
    );

    /**
     * POST to /api/games to create a game.
     * @param body The game body.
     * @return The new game.
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
    @PostMapping(value = "/api/games", produces = "application/json")
    ResponseEntity<DataResponseGame> postGame(
            @RequestHeader("Authorization")
                String token,
            @Valid @RequestBody
                NewGameBody body
    );

    /**
     * PATCH to /api/games/{game_id} to edit a game.
     * @param gameId The game id.
     * @param body The game body.
     * @return The edited game.
     */
    @Operation(summary = "Edit a game", description = "Edit a game", parameters = {
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
    @PatchMapping(value = "/api/games/{game_id}", produces = "application/json")
    ResponseEntity<DataResponseGame> patchGame(
            @RequestHeader("Authorization")
                String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The game id")
            @PathVariable("game_id")
                Long gameId,
            @Valid @RequestBody
                UpdateGameBody body
    );

    /**
     * DELETE to /api/games/{game_id} to delete a game.
     * @param gameId The game id
     */
    @Operation(summary = "Delete a game", description = "Delete a game", parameters = {
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
    @DeleteMapping(value = "/api/games/{game_id}", produces = "application/json")
    ResponseEntity<Void> deleteGame(
            @RequestHeader("Authorization")
                String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The game id")
            @PathVariable("game_id")
                Long gameId
    );
}
