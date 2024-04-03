package quantum.service;

import org.springframework.data.domain.Pageable;
import quantum.dto.userGames.NewUserGameBody;
import quantum.dto.userGames.UpdateUserGameBody;
import quantum.dto.userGames.UserGameResponse;
import quantum.dto.userGames.UserGamesListResponse;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.model.Game;

/**
 * Service for {@link Game} entity.
 */
public interface UserGamesService {

    /**
     * Gets user games.
     *
     * @param username The username.
     * @param category The category.
     * @param pageable The pageable.
     * @return the games
     */
    UserGamesListResponse getUserGames(String username, String category, Pageable pageable);

    /**
     * Add game to a user.
     *
     * @param username   The username.
     * @param gameSgdbId The game sgdb id.
     * @param body       The body
     */
    UserGameResponse postUserGame(String username, Long gameSgdbId, NewUserGameBody body);

    /**
     * Add game to a user.
     *
     * @param username The username.
     * @param body     The body
     */
    UserGamesListResponse importUserGames(String username, UserGamesImportList body);

    /**
     * Patch a game from a user.
     *
     * @param username The username.
     * @param gameId   The game id.
     * @param body     The body.
     */
    UserGameResponse updateUserGame(String username, Long gameId, UpdateUserGameBody body);

    /**
     * Delete game from a user.
     *
     * @param username The username.
     * @param gameId   The game id.
     */
    void deleteUserGame(String username, Long gameId);

}
