package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.user.UserListResponse;
import quantum.dto.user.UserResponse;
import quantum.dto.usergames.NewUserGameBody;
import quantum.dto.usergames.UpdateUserGameBody;
import quantum.dto.usergames.UserGamesListResponse;
import quantum.dto.usergames.UserGameResponse;
import quantum.model.UserGame;
import quantum.service.UserGamesService;
import quantum.service.UserService;
import quantum.web.api.UserGamesApi;

/**
 * Controller for {@link UserGame} entity.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserGamesController implements UserGamesApi {

    private  final UserGamesService userGamesService;

    /**
     * GET to /api/user/{user_id}/games to fetch a user game list.
     * @param token The token with the authentication information.
     * @param username The username.
     * @param pageable the pageable
     * @return The list of games
     */
    @Override
    public ResponseEntity<UserGamesListResponse> getUserGames(String token, String username, Pageable pageable) {
        log.info("[CONTROLLER] - Searching user games");
        UserGamesListResponse result = userGamesService.getUserGames(username, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * POST to /api/user/{user_id}/games add a game to a user
     * @param token The token with the authentication information.
     * @param username The username.
     * @param gameSgbdId The sgbd game id.
     * @param body The user game body.
     * @return The new user game.
     */
    @Override
    public ResponseEntity<UserGameResponse> postUserGame(String token, String username, Long gameSgbdId, NewUserGameBody body) {
        log.info("[CONTROLLER] - Adding game to user");
        UserGameResponse result = userGamesService.postUserGame( body, username, gameSgbdId);
        return ResponseEntity.ok(result);
    }

    /**
     * PATCH to /api/user/{user_id}/games/{game_id} to edit a game from a user.
     * @param token The token with the authentication information.
     * @param username The username.
     * @param gameSgbdId The sgbd game id.
     * @param body The user game body.
     * @return The edited user game.
     */
    @Override
    public ResponseEntity<UserGameResponse> patchUserGame(String token, String username, Long gameSgbdId, UpdateUserGameBody body) {
        log.info("[CONTROLLER] - Editing game from user");
        UserGameResponse result = userGamesService.updateUserGame(username, gameSgbdId, body);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE to /api/user/{user_id}/games/{game_id} to delete a game from a user.
     * @param token The token with the authentication information.
     * @param username The username.
     * @param gameSgbdId The game sgbd id.
     */
    @Override
    public ResponseEntity<Void> deleteUserGame(String token, String username, Long gameSgbdId) {
        log.info("[CONTROLLER] - Deleting game from user");
        userGamesService.deleteUserGame(username, gameSgbdId);
        return ResponseEntity.noContent().build();
    }
}
