package quantum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.GameResponse;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.GamesMapping;
import quantum.model.Game;
import quantum.repository.GameRepository;
import quantum.service.GameService;

import java.util.List;
import java.util.Optional;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;

/**
 * Service implementation for {@link Game} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final GamesMapping mapper;

    //------------------------------------- PUBLIC METHODS -------------------------------------//

    /**
     * Retrieve Games.
     *
     * @param pageable The pageable
     * @return The games
     */
    @Override
    public GameListResponse getGames(Pageable pageable) {
        Page<Game> result;

        try {
            log.info("[SERVICE] - [GAME SEARCH] - Searching games");
            result = repository.findAll(pageable);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (result.isEmpty()) {
            throw new EntityNotFoundException();
        }

        // Map entity to response and return
        return GameListResponse.builder()
                .games(result.get().map(mapper::map).toList())
                .build();
    }


    /**
     * Find a game by id.
     *
     * @param id The id of the game to find.
     * @return The game.
     */
    @Override
    public Game findGameById(Long id, boolean sgdbId) {
        Optional<Game> game;
        try {
            log.info("[SERVICE] - [GAME SEARCH] - Searching game: {}", id);
            game = sgdbId ? repository.findBySgdbId(id) : repository.findById(id);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (game.isEmpty() && !sgdbId) {
            throw new EntityNotFoundException();
        }

        return game.orElse(null);
    }

    /**
     * Create a new game.
     *
     * @param body           The body.
     * @param retrieveObject Whether to retrieve the object or not.
     * @return The game entity or the response.
     */
    @Override
    public Object postGame(NewGameBody body, boolean retrieveObject) {
        // Generate new game
        Game newGame = generateNewGame(body);

        try {
            log.info("[SERVICE] - [GAME CREATION] - Saving game: {}", newGame);
            newGame = repository.save(newGame);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseConnectionException(DATA_INTEGRITY_ERROR, ex);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        return retrieveObject ? newGame : mapper.map(newGame);
    }

    /**
     * Saves a list of games.
     *
     * @param games The games to save.
     */
    @Override
    public void postGames(List<Game> games){
        long startTime = System.currentTimeMillis();

        try {
            log.info("[SERVICE] - [GAME CREATION] - Saving {} games", games.size());
            repository.saveAll(games);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseConnectionException(DATA_INTEGRITY_ERROR, ex);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        } finally {
            long endTime = System.currentTimeMillis(); // End the timer
            long duration = endTime - startTime; // Calculate the duration in milliseconds
            log.info("[SERVICE] - [GAME CREATION] - Time taken to save games: {} ms", duration);
        }
    }

    /**
     * Update a game.
     *
     * @param id   The id
     * @param body The body
     * @return The game
     */
    @Override
    public GameResponse updateGame(Long id, UpdateGameBody body) {

        // Find the game
        Game gameToUpdate = findGameById(id, false);

        // Update the game content
        updateGameContent(body, gameToUpdate);

        try {
            log.info("[SERVICE] - [GAME UPDATE] - Saving game: {}", gameToUpdate);
            gameToUpdate = repository.save(gameToUpdate);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(gameToUpdate);
    }

    /**
     * Delete a game.
     *
     * @param id the id
     */
    @Override
    public void deleteGame(Long id) {
        try {
            log.info("[SERVICE] - [GAME DELETE] - Deleting game: {}", id);
            repository.delete(findGameById(id, false));
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    //------------------------------------- PRIVATE METHODS -------------------------------------//

    /**
     * Generate a new game.
     *
     * @param body The body
     * @return The game
     */
    private Game generateNewGame(NewGameBody body) {
        return Game.builder()
                .name(body.getName())
                .tags(body.getTags())
                .image(body.getImage())
                .sgdbId(body.getSgdbId())
                .build();
    }

    /**
     * Update a game.
     *
     * @param body The body
     */
    private void updateGameContent(UpdateGameBody body, Game gameToUpdate) {
        if (body.getName() != null) {
            gameToUpdate.setName(body.getName());
        }
        if (body.getTags() != null) {
            gameToUpdate.setTags(body.getTags());
        }
        if (body.getImage() != null) {
            gameToUpdate.setImage(body.getImage());
        }
    }


}
