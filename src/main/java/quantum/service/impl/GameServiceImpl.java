package quantum.service.impl;

import quantum.exceptions.DatabaseConnectionException;
import jakarta.persistence.EntityNotFoundException;
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
import quantum.dto.game.DataResponseGame;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.mapping.GamesMapping;
import quantum.model.Game;
import quantum.repository.GameRepository;
import quantum.service.GameService;

import java.util.Optional;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;
import static quantum.constant.ErrorConstants.ENTITY_NOT_FOUND_ERROR;

/**
 * Service implementation for {@link Game} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GamesMapping mapper;

    // TODO: Manual id generation

    /**
     * Retrieve Games.
     * @param pageable the pageable
     * @return the games
     */
    @Override
    public GameListResponse getGames(Pageable pageable) {
        Page<Game> result;

        try {
            result = gameRepository.findAll(pageable);
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
     * Create a new game.
     * @param body the body
     * @return the game
     */
    @Override
    public DataResponseGame postGame(NewGameBody body) {

        // Generate new game
        Game newGame = generateNewGame(body);

        try {
            log.debug("[GAME CREATION] - Saving game: {}", newGame);
            newGame = gameRepository.save(newGame);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseConnectionException(DATA_INTEGRITY_ERROR, ex);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(newGame);
    }

    /**
     * Update a game.
     * @param id the id
     * @param body the body
     * @return the game
     */
    @Override
    public DataResponseGame updateGame(Long id, UpdateGameBody body) {

        // Find the game
        Game gameToUpdate = findGameById(id);

        // Update the game content
        updateGameContent(body, gameToUpdate);

        try {
            log.debug("[GAME UPDATE] - Saving game: {}", gameToUpdate);
            gameToUpdate = gameRepository.save(gameToUpdate);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(gameToUpdate);
    }

    /**
     * Delete a game.
     * @param id the id
     */
    @Override
    public void deleteGame(Long id) {
        try {
            gameRepository.deleteById(validateGame(id));
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    /**
     * Make sure a game exists.
     * @param id The id of the game to validate.
     */
    public Long validateGame(Long id) {
        boolean exists;
        try {
            exists = gameRepository.existsById(id);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        if (!exists) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR);
        }
        return id;
    }

    /**
     * Generate a new game.
     * @param body the body
     * @return the game
     */
    private Game generateNewGame(NewGameBody body) {
        return Game.builder()
                .name(body.getName())
                .description(body.getDescription())
                .tags(body.getTags())
                .image(body.getImage())
                .build();
    }

    /**
     * Update a game.
     *
     * @param body the body
     */
    private void updateGameContent(UpdateGameBody body, Game gameToUpdate) {
        if (body.getName() != null) {
            gameToUpdate.setName(body.getName());
        }
        if (body.getDescription() != null) {
            gameToUpdate.setDescription(body.getDescription());
        }
        if (body.getTags() != null) {
            gameToUpdate.setTags(body.getTags());
        }
        if (body.getImage() != null) {
            gameToUpdate.setImage(body.getImage());
        }
    }

    /**
     * Find a game by id.
     * @param id The id of the game to find.
     * @return The game.
     */
    public Game findGameById(Long id) {
        Optional<Game> game;
        try{
            game = gameRepository.findById(id);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (game.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return game.get();
    }
}
