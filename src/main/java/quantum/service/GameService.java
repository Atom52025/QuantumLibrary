package quantum.service;

import org.springframework.data.domain.Pageable;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.GameResponse;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.model.Game;

/**
 * Service for {@link Game} entity.
 */
public interface GameService {

        /**
        * Gets games.
        * @param pageable the pageable
        * @return the games
        */
        GameListResponse getGames(Pageable pageable);

        /**
         * Post game.
         *
         * @param body the body
         */
        Game postGame(NewGameBody body);

        /**
         * Patch game.
         * @param id the id
         * @param body the body
         */
        GameResponse updateGame(Long id, UpdateGameBody body);

        /**
         * Delete game.
         * @param id the id
         */
        void deleteGame(Long id);

        /**
         * Find a game by id.
         * @param id The id of the game to find.
         * @return The game.
         */
        Game findGameById(Long id);

        /**
         * Find a game by id.
         * @param sgbdId The sgbd id of the game to find.
         * @return The game.
         */
        Game findGameBySgbdId(Long sgbdId) ;

}
