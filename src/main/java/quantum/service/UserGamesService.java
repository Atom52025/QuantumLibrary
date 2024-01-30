package quantum.service;

import org.springframework.data.domain.Pageable;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
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
         * @param userId The user id.
         * @param pageable the pageable
        * @return the games
        */
        UserGamesListResponse getUserGames(Long userId, Pageable pageable);

        /**
         * Add game to a user.
         * @param userId The user id.
         * @param body the body
         */
        UserGameResponse postUserGame(Long userId, NewUserGameBody body);

        /**
         * Patch a game from a user.
         * @param userId The user id.
         * @param gameId The game id.
         * @param body The body.
         */
        UserGameResponse updateUserGame(Long userId, Long gameId, UpdateUserGameBody body);

        /**
         * Delete game from a user.
         * @param userId The user id.
         * @param gameId The game id.
         */
        void deleteUserGame(Long userId, Long gameId);

}
