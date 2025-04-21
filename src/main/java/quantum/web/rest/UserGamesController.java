package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.userGames.*;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.model.UserGame;
import quantum.security.jwt.JwtUtil;
import quantum.service.UserGamesService;
import quantum.web.api.UserGamesApi;

import java.util.List;

/**
 * Controller for {@link UserGame} entity.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserGamesController implements UserGamesApi {

    private final UserGamesService service;

    /**
     * GET to /api/user/games to fetch a user game list.
     *
     * @param token    The token with the authentication information.
     * @param category The category.
     * @param pageable The pageable.
     * @return The list of games
     */
    @Override
    public ResponseEntity<UserGamesListResponse> getUserGames(String token, String category, Pageable pageable) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Searching user games");
        UserGamesListResponse result = service.getUserGames(username, category, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * GET to /api/user/onlineGames to fetch a user game online list.
     *
     * @param token The token with the authentication information.
     * @return The list of games
     */
    @Override
    public ResponseEntity<List<UserGame>> getOnlineGames(String token) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Searching online games on user");
        List<UserGame> result = service.getOnlineGames(username);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/user/games add a game to a user
     *
     * @param token      The token with the authentication information.
     * @param gameSgdbId The sgdb game id.
     * @param body       The user game body.
     * @return The new user game.
     */
    @Override
    public ResponseEntity<UserGameResponse> postUserGame(String token, Long gameSgdbId, NewUserGameBody body) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Adding game to user");
        UserGameResponse result = service.postUserGame(username, gameSgdbId, body);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/user/games/import to import a list of games to a user.
     *
     * @param token The token with the authentication information.
     * @param body  Import list body.
     * @return The new user games.
     */
    @Override
    public ResponseEntity<UserGamesListResponse> importUserGames(String token, UserGamesImportList body) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Importing games to user");
        UserGamesListResponse result = service.importUserGames(username, body);
        return ResponseEntity.ok(result);
    }


    /**
     * PATCH to /api/user/games/{game_id} to edit a game from a user.
     *
     * @param token      The token with the authentication information.
     * @param gameSgdbId The sgdb game id.
     * @param body       The user game body.
     * @return The edited user game.
     */
    @Override
    public ResponseEntity<UserGameResponse> patchUserGame(String token, Long gameSgdbId, UpdateUserGameBody body) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Editing game from user");
        UserGameResponse result = service.updateUserGame(username, gameSgdbId, body);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE to /api/user/games/{game_id} to delete a game from a user.
     *
     * @param token      The token with the authentication information.
     * @param gameSgdbId The game sgdb id.
     */
    @Override
    public ResponseEntity<Void> deleteUserGame(String token, Long gameSgdbId) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Deleting game from user");
        service.deleteUserGame(username, gameSgdbId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET to /api/user/games/stats to fetch user games statistics.
     *
     * @param token The token with the authentication information.
     * @return The user games statistics.
     */
    @Override
    public ResponseEntity<StatsResponse> getStats(String token) {
        String username = JwtUtil.getUserDetails().getUsername();

        log.info("[CONTROLLER] - Getting user games statistics");
        StatsResponse result = service.getStats(username);
        return ResponseEntity.ok(result);
    }
}
