package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.GameResponse;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.mapping.GamesMapping;
import quantum.model.Game;
import quantum.service.GameService;
import quantum.web.api.GameApi;

/**
 * Controller for {@link Game} entity.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameController implements GameApi {

    private  final GameService gameService;
    private final GamesMapping mapper;

    /**
     * GET to /api/games to fetch games list.
     * @param pageable the pageable
     * @return The list of games
     */
    @Override
    public ResponseEntity<GameListResponse> getGames(String token, Pageable pageable) {
        log.info("[CONTROLLER] - Searching games");
        GameListResponse result = gameService.getGames(pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/games to create a game.
     * @param body The game body.
     * @return The new game.
     */
    @Override
    public ResponseEntity<GameResponse> postGame(String token, NewGameBody body) {
        log.info("[CONTROLLER] - Creating game");
        Game result = gameService.postGame(body);
        return ResponseEntity.ok(mapper.map(result));
    }

    /**
     * PUT to /api/games/{id} to update a game.
     * @param id The id of the game to update.
     * @param body The game body.
     * @return The updated game.
     */
    @Override
    public ResponseEntity<GameResponse> patchGame(String token, Long id, UpdateGameBody body) {
        log.info("[CONTROLLER] - Updating game");
        GameResponse result = gameService.updateGame(id, body);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE to /api/games/{id} to delete a game.
     * @param id The id of the game to delete.
     * @return The deleted game.
     */
    @Override
    public ResponseEntity<Void> deleteGame(String token, Long id) {
        log.info("[CONTROLLER] - Deleting game");
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
