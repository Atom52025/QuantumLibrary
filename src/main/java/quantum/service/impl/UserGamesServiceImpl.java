package quantum.service.impl;

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
import quantum.dto.game.NewGameBody;
import quantum.dto.usergames.NewUserGameBody;
import quantum.dto.usergames.UpdateUserGameBody;
import quantum.dto.usergames.UserGameResponse;
import quantum.dto.usergames.UserGamesListResponse;
import quantum.exceptions.DatabaseConnectionException;
import quantum.mapping.UserGamesMapping;
import quantum.model.Game;
import quantum.model.User;
import quantum.model.UserGame;
import quantum.repository.UserGamesRepository;
import quantum.service.GameService;
import quantum.service.UserGamesService;
import quantum.service.UserService;

import java.util.List;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;
import static quantum.constant.ErrorConstants.ENTITY_NOT_FOUND_ERROR;


/**
 * Service implementation for {@link UserGame} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserGamesServiceImpl implements UserGamesService {

    private final UserGamesRepository userGamesRepository;
    private final UserGamesMapping mapper;
    private final UserService userService;
    private final GameService gameService;

    /**
     * Gets user games.
     * @param username The username.
     * @param pageable the pageable
     * @return the games
     */
    @Override
    public UserGamesListResponse getUserGames(String username, Pageable pageable) {
        Page<UserGame> result;

        try {
            result = userGamesRepository.findByUser_Username(username, pageable);
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
     * @param username The username.
     * @param gameSgbdId The game sgbd id.
     * @param body the body
     */
    @Override
    public UserGameResponse postUserGame(NewUserGameBody body, String username, Long gameSgbdId) {

        // Search if game is in the database
        Game game = gameService.findGameBySgbdId(gameSgbdId);

        // If game is not in the database, create it
        if (game == null){
            game = gameService.postGame(
                    NewGameBody.builder()
                    .name(body.getName())
                    .image(body.getImage())
                    .tags(body.getTags())
                    .sgbdId(gameSgbdId)
                    .build());
        }

        // Generate new user game
        UserGame newUserGame = generateNewGame(body, username, game);

        try {
            log.debug("[USER GAME CREATION] - Saving user game: {}", newUserGame);
            newUserGame = userGamesRepository.save(newUserGame);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseConnectionException(DATA_INTEGRITY_ERROR, ex);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(newUserGame);
    }

    /**
     * Patch a game from a user.
     * @param username The username.
     * @param gameId The game id.
     * @param body The body.
     */
    @Override
    public UserGameResponse updateUserGame(String username, Long gameId, UpdateUserGameBody body) {

        // Find the user game
        UserGame userGameToUpdate = findUserGame(gameId, username);

        // Update the user game content
        updateUserGameContent(body, userGameToUpdate);

        try {
            log.debug("[GAME UPDATE] - Saving game: {}", userGameToUpdate);
            userGameToUpdate = userGamesRepository.save(userGameToUpdate);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(userGameToUpdate);
    }

    /**
     * Delete game from a user.
     * @param username The username.
     * @param gameId The game id.
     */
    @Override
    public void deleteUserGame(String username, Long gameId) {
        try {
            userGamesRepository.delete(findUserGame(gameId, username));
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }
    /**
     * Find a user game by game id and username.
     * @param gameId The game id.
     * @param username The username.
     */
    private UserGame findUserGame(Long gameId, String username) {
        UserGame userGame;

        // Try to find the entity
        try {
            userGame = userGamesRepository.findByUser_UsernameAndGame_Id(username, gameId);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (userGame == null) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR);
        }
        return userGame;
    }


    /**
     * Generate a new game.
     * @param username The username.
     * @param game The game.
     * @param body the body
     * @return the game
     */
    private UserGame generateNewGame(NewUserGameBody body, String username, Game game) {
        User user = userService.findUser(username);
        if (user == null || game == null) throw new EntityNotFoundException();
        return UserGame.builder()
                .user(user)
                .game(game)
                .timePlayed(body.getTimePlayed())
                .build();
    }

    /**
     * Update a game.
     *
     * @param body the body
     */
    private void updateUserGameContent(UpdateUserGameBody body, UserGame userGameToUpdate) {
        if (body.getTimePlayed() != null) {
            userGameToUpdate.setTimePlayed(body.getTimePlayed());
        }
    }
}
