package quantum.service;

import org.springframework.data.domain.Pageable;
import quantum.dto.usergames.NewUserGameBody;
import quantum.dto.usergames.UpdateUserGameBody;
import quantum.dto.usergames.UserGameResponse;
import quantum.dto.usergames.UserGamesListResponse;
import quantum.model.Game;

/**
 * Service for {@link Game} entity.
 */
public interface UserGamesService {

        /**
        * Gets user games.
         * @param username The username.
         * @param category The category.
         * @param pageable The pageable.
        * @return the games
        */
        UserGamesListResponse getUserGames(String username, String category, Pageable pageable);

        /**
         * Add game to a user.
         * @param username The username.
         * @param gameSgbdId The game sgbd id.
         * @param body the body
         */
        UserGameResponse postUserGame(NewUserGameBody body, String username, Long gameSgbdId);

        /**
         * Patch a game from a user.
         * @param username The username.
         * @param gameId The game id.
         * @param body The body.
         */
        UserGameResponse updateUserGame(String username, Long gameId, UpdateUserGameBody body);

        /**
         * Delete game from a user.
         * @param username The username.
         * @param gameId The game id.
         */
        void deleteUserGame(String username, Long gameId);

}
