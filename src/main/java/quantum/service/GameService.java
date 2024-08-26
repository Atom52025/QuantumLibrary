package quantum.service;

import org.springframework.data.domain.Pageable;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.GameResponse;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.model.Game;

import java.util.List;

/**
 * Service for {@link Game} entity.
 */
public interface GameService {

    /**
     * Gets games.
     *
     * @param pageable the pageable
     * @return the games
     */
    GameListResponse getGames(Pageable pageable);

    /**
     * Find a game by id.
     *
     * @param id The id of the game to find.
     * @param sgdbId Wheter the id is the sgdb id or not.
     * @return The game.
     */
    Game findGameById(Long id, boolean sgdbId);

    /**
     * Create a new game.
     *
     * @param body           The body.
     * @param retrieveObject Whether to retrieve the object or not.
     * @return The game entity or the response.
     */
    Object postGame(NewGameBody body, boolean retrieveObject);

    /**
     * Saves a list of games.
     *
     * @param games The games to save.
     */
    void postGames(List<Game> games);

    /**
     * Patch game.
     *
     * @param id   the id
     * @param body The body
     */
    GameResponse updateGame(Long id, UpdateGameBody body);

    /**
     * Delete game.
     *
     * @param id the id
     */
    void deleteGame(Long id);



}
