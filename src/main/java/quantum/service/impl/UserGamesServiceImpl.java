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
import quantum.dto.userGames.NewUserGameBody;
import quantum.dto.userGames.UpdateUserGameBody;
import quantum.dto.userGames.UserGameResponse;
import quantum.dto.userGames.UserGamesListResponse;
import quantum.dto.userGames.steamImport.UserGameImport;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.exceptions.DatabaseConnectionException;
import quantum.mapping.UserGamesMapping;
import quantum.model.Game;
import quantum.model.User;
import quantum.model.UserGame;
import quantum.repository.UserGamesRepository;
import quantum.service.GameService;
import quantum.service.UserGamesService;
import quantum.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @param category The category.
     * @param pageable The pageable.
     * @return The games.
     */
    @Override
    public UserGamesListResponse getUserGames(String username, String category, Pageable pageable) {
        Page<UserGame> result;

        try {
            log.debug("[USER GAME FETCHING] - Searching games of a user with username: {} and category: {}", username, category);
            switch (category) {
                case "backlog1" -> result = userGamesRepository.findByUser_UsernameAndBacklog(username, 1, pageable);
                case "backlog2" -> result = userGamesRepository.findByUser_UsernameAndBacklog(username, 2, pageable);
                case "backlog3" -> result = userGamesRepository.findByUser_UsernameAndBacklog(username, 3, pageable);
                case "favorite" -> result = userGamesRepository.findByUser_UsernameAndFavorite(username, true, pageable);
                case "finished" -> result = userGamesRepository.findByUser_UsernameAndFinished(username, true, pageable);
                case "completed" -> result = userGamesRepository.findByUser_UsernameAndCompleted(username, pageable);
                default -> result = userGamesRepository.findByUser_Username(username, pageable);
            }
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return UserGamesListResponse.builder()
                .games(result.stream().map(mapper::map).toList())
                .build();
    }

    /**
     * Add game to a user.
     * @param username   The username.
     * @param gameSgbdId The game sgbd id.
     * @param body       The body.
     */
    @Override
    public UserGameResponse postUserGame(String username, Long gameSgbdId, NewUserGameBody body) {

        // Check if game is already added
        Optional<UserGame> userGame = userGamesRepository.findByUser_UsernameAndGame_SgbdId(username, gameSgbdId);
        if (userGame.isPresent()) {
            throw new DataIntegrityViolationException(DATA_INTEGRITY_ERROR);
        }

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
        UserGame newUserGame = generateNewUserGame(body, username, game);

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
     * Add game to a user.
     * @param username The username.
     * @param body     The body
     */
    @Override
    public UserGamesListResponse importUserGames( String username, UserGamesImportList body){
        UserGamesListResponse response = UserGamesListResponse.builder().games(new ArrayList<>()).build();
        for (UserGameImport game : body.getGames()) {
            NewUserGameBody newUserGameBody = NewUserGameBody.builder()
                    .name(game.getName())
                    .timePlayed(game.getTimePlayed())
                    .image(game.getImage())
                    .tags("imported")
                    .build();
            try {
                response.getGames().add(postUserGame(username, game.getSgbdId(), newUserGameBody));
            } catch (DataIntegrityViolationException ex) {
                log.debug("[USER GAME IMPORT] - Game already exists: {}", game.getName());
            }
        }
        return response;
    }

    /**
     * Patch a game from a user.
     * @param username The username.
     * @param gameId   The game id.
     * @param body     The body.
     */
    @Override
    public UserGameResponse updateUserGame(String username, Long gameId, UpdateUserGameBody body) {

        // Find the user game
        UserGame userGameToUpdate = findUserGame(gameId, username);

        // Update the user game content
        updateUserGameContent(body, userGameToUpdate);

        try {
            log.debug("[USER GAME UPDATE] - Saving user game: {}", userGameToUpdate);
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
     * @param gameId   The game id.
     */
    @Override
    public void deleteUserGame(String username, Long gameId) {
        try {
            log.debug("[USER GAME DELETE] - Deleting user game: {} of user with username: {}", gameId, username);
            userGamesRepository.delete(findUserGame(gameId, username));
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }
    /**
     * Find a user game by game id and username.
     * @param gameId   The game id.
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
     * @param game     The game.
     * @param body     The body
     * @return The game.
     */
    private UserGame generateNewUserGame(NewUserGameBody body, String username, Game game) {
        User user = userService.findUser(username);
        if (user == null || game == null) throw new EntityNotFoundException();
        String allTags = body.getTags() + ", " + game.getTags();
        String uniqueTags = Arrays.stream(allTags.split(", "))
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.joining(", "));

        return UserGame.builder()
                .user(user)
                .game(game)
                .timePlayed(body.getTimePlayed())
                .tags(uniqueTags)
                .achivements(body.getAchivements())
                .totalAchivements(body.getTotalAchivements())
                .finished(body.getFinished())
                .favorite(body.getFavorite())
                .backlog(body.getBacklog())
                .build();
    }

    /**
     * Update a user game.
     * @param body The body
     */
    private void updateUserGameContent(UpdateUserGameBody body, UserGame userGameToUpdate) {
        if (body.getTimePlayed() != null) {
            userGameToUpdate.setTimePlayed(body.getTimePlayed());
        }

        if (body.getTags() != null) {
            userGameToUpdate.setTags(body.getTags());
        }

        if (body.getImage() != null) {
            userGameToUpdate.setImage(body.getImage());
        }

        if (body.getAchivements() != null) {
            userGameToUpdate.setAchivements(body.getAchivements());
        }

        if (body.getTotalAchivements() != null) {
            userGameToUpdate.setTotalAchivements(body.getTotalAchivements());
        }

        if (body.getFinished() != null) {
            userGameToUpdate.setFinished(body.getFinished());
        }

        if (body.getFavorite() != null) {
            userGameToUpdate.setFavorite(body.getFavorite());
        }

        if (body.getBacklog() != null) {
            userGameToUpdate.setBacklog(body.getBacklog());
        }
    }
}
