package quantum.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.dto.usergames.NewUserGameBody;
import quantum.dto.usergames.UpdateUserGameBody;
import quantum.dto.usergames.UserGameResponse;
import quantum.dto.usergames.UserGamesListResponse;
import quantum.exceptions.DatabaseConnectionException;
import quantum.mapping.UserGamesMapping;
import quantum.model.Game;
import quantum.model.UserGame;
import quantum.repository.UserGamesRepository;
import quantum.service.UserGamesService;

import java.util.List;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;


/**
 * Service implementation for {@link UserGame} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserGamesServiceImpl implements UserGamesService {

    private final UserGamesRepository userGamesRepository;
    private final UserGamesMapping mapper;

    /**
     * Gets user games.
     * @param userId The user id.
     * @param pageable the pageable
     * @return the games
     */
    @Override
    public UserGamesListResponse getUserGames(Long userId, Pageable pageable) {
        List<UserGame> result;

        try {
            result = userGamesRepository.findByUserId(userId, pageable);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (result.isEmpty()) {
            throw new EntityNotFoundException();
        }

        // Map entity to response and return
        return UserGamesListResponse.builder()
                .games(result.stream().map(mapper::map).toList())
                .build();
    }

    /**
     * Add game to a user.
     * @param userId The user id.
     * @param body the body
     */
    @Override
    public UserGameResponse postUserGame(Long userId, NewUserGameBody body) {

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
     * Patch a game from a user.
     * @param userId The user id.
     * @param gameId The game id.
     * @param body The body.
     */
    @Override
    public UserGameResponse updateUserGame(Long userId, Long gameId, UpdateUserGameBody body) {
        return null;
    }

    /**
     * Delete game from a user.
     * @param userId The user id.
     * @param gameId The game id.
     */
    @Override
    public void deleteUserGame(Long userId, Long gameId) {

    }
}
