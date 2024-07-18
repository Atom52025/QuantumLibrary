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
import quantum.dto.game.NewGameBody;
import quantum.dto.userGames.NewUserGameBody;
import quantum.dto.userGames.UpdateUserGameBody;
import quantum.dto.userGames.UserGameResponse;
import quantum.dto.userGames.UserGamesListResponse;
import quantum.dto.userGames.steamImport.UserGameImport;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.UserGamesMapping;
import quantum.model.Game;
import quantum.model.User;
import quantum.model.UserGame;
import quantum.repository.UserGamesRepository;
import quantum.service.GameService;
import quantum.service.UserGamesService;
import quantum.service.UserService;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static quantum.constant.ErrorConstants.DATA_INTEGRITY_ERROR;
import static quantum.constant.ErrorConstants.ENTITY_NOT_FOUND_ERROR;


/**
 * Service implementation for {@link UserGame} entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserGamesServiceImpl implements UserGamesService {

    private final UserGamesRepository repository;
    private final UserGamesMapping mapper;
    private final UserService userService;
    private final GameService gameService;

    //------------------------------------- PUBLIC METHODS -------------------------------------//

    /**
     * Gets user games.
     *
     * @param username The username.
     * @param category The category.
     * @param pageable The pageable.
     * @return The games.
     */
    @Override
    public UserGamesListResponse getUserGames(String username, String category, Pageable pageable) {
        Page<UserGame> result;

        try {
            log.info("[SERVICE] - [USER GAME FETCHING] - Searching games of a user with username: {} and category: {}", username, category);
            switch (category) {
                case "backlog1" -> result = repository.findByUser_UsernameAndBacklog(username, 1, pageable);
                case "backlog2" -> result = repository.findByUser_UsernameAndBacklog(username, 2, pageable);
                case "backlog3" -> result = repository.findByUser_UsernameAndBacklog(username, 3, pageable);
                case "favorite" -> result = repository.findByUser_UsernameAndFavorite(username, true, pageable);
                case "finished" -> result = repository.findByUser_UsernameAndFinished(username, true, pageable);
                case "completed" -> result = repository.findByUser_UsernameAndCompleted(username, pageable);
                default -> result = repository.findByUser_Username(username, pageable);
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
     * Gets user games.
     *
     * @param username The username.
     * @return the games
     */
    @Override
    public List<UserGame> getOnlineGames(String username) {
        return repository.findByUser_UsernameAndTagsIn(username, Set.of("Co-op","Online Co-Op","Local Co-Op","Multiplayer","Local Multiplayer"));
    }

    /**
     * Gets games common to list users.
     *
     * @param users The list of users.
     * @return the games
     */
    @Override
    public Set<Game> getCommonGames(List<User> users){
        // Create a list of CompletableFuture for fetching games of each user asynchronously
        List<CompletableFuture<List<UserGame>>> userGameFutures = users.stream()
                .map(user -> CompletableFuture.supplyAsync(() -> getOnlineGames(user.getUsername())))
                .toList();

        // Wait for all CompletableFuture to complete and map results to a list of games
        List<List<Game>> userGames = userGameFutures.stream()
                .map(CompletableFuture::join)
                .map(list -> list.stream().map(UserGame::getGame).toList())
                .toList();

        // Find common games
        Set<Game> commonGames = new HashSet<>(userGames.get(0));

        for (List<Game> games : userGames.subList(1, userGames.size())) {
            Set<Game> gamesSet = new HashSet<>(games);
            commonGames.retainAll(gamesSet);
        }

        return commonGames;
    }

    /**
     * Add game to a user.
     *
     * @param username   The username.
     * @param gameSgdbId The game sgdb id.
     * @param body       The body.
     */
    @Override
    public UserGameResponse postUserGame(String username, Long gameSgdbId, NewUserGameBody body) {

        // Check if game is already added
        Optional<UserGame> userGame = repository.findByUser_UsernameAndGame_SgdbId(username, gameSgdbId);
        if (userGame.isPresent()) {
            throw new DataIntegrityViolationException(DATA_INTEGRITY_ERROR);
        }

        // Search if game is in the database
        Game game = gameService.findGameById(gameSgdbId, true);

        // If game is not in the database, create it
        if (game == null) {
            game = (Game) gameService.postGame(
                    NewGameBody.builder()
                            .name(body.getName())
                            .image(body.getImage())
                            .tags(body.getTags())
                            .sgdbId(gameSgdbId)
                            .build(),
                    true);
        }

        // Generate new user game
        UserGame newUserGame = generateNewUserGame(body, username, game);

        try {
            log.info("[SERVICE] - [USER GAME CREATION] - Saving user game: {}", newUserGame);
            newUserGame = repository.save(newUserGame);
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
     *
     * @param username The username.
     * @param body     The body
     */
    @Override
    public UserGamesListResponse importUserGames(String username, UserGamesImportList body) {
        UserGamesListResponse response = UserGamesListResponse.builder().games(new ArrayList<>()).build();
        for (UserGameImport game : body.getGames()) {
            NewUserGameBody newUserGameBody = NewUserGameBody.builder()
                    .name(game.getName())
                    .timePlayed(game.getTimePlayed())
                    .image(game.getImage())
                    .tags(new HashSet<>(game.getTags()))
                    .build();
            try {
                log.info("[SERVICE] - [USER GAME IMPORT] - Importing game: {}", game.getName());
                response.getGames().add(postUserGame(username, game.getSgdbId(), newUserGameBody));
            } catch (DataIntegrityViolationException ex) {
                log.info("[SERVICE] - [USER GAME IMPORT] - Game already in library: {}", game.getName());
            }
        }
        return response;
    }

    /**
     * Patch a game from a user.
     *
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
            log.info("[SERVICE] - [USER GAME UPDATE] - Saving user game: {}", userGameToUpdate);
            userGameToUpdate = repository.save(userGameToUpdate);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Map entity to response and return
        return mapper.map(userGameToUpdate);
    }

    /**
     * Delete game from a user.
     *
     * @param username The username.
     * @param gameId   The game id.
     */
    @Override
    public void deleteUserGame(String username, Long gameId) {
        try {
            log.info("[SERVICE] - [USER GAME DELETE] - Deleting game: {} of user: {}", gameId, username);
            repository.delete(findUserGame(gameId, username));
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

    //------------------------------------- PRIVATE METHODS -------------------------------------//

    /**
     * Find a user game by game id and username.
     *
     * @param gameId   The game id.
     * @param username The username.
     * @return The user game found.
     */
    private UserGame findUserGame(Long gameId, String username) {
        Optional<UserGame> userGame;

        // Try to find the entity
        try {
            log.info("[SERVICE] - [USER GAME SEARCH] - Searching game: {} of user: {}", gameId, username);
            userGame = repository.findByUser_UsernameAndGame_Id(username, gameId);
        } catch (JpaSystemException | QueryTimeoutException | JDBCConnectionException | DataException ex) {
            throw new DatabaseConnectionException(ex);
        }

        // Check if there is any result
        if (userGame.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_ERROR);
        }
        return userGame.get();
    }


    /**
     * Generate a new game.
     *
     * @param username The username.
     * @param game     The game.
     * @param body     The body
     * @return The game.
     */
    private UserGame generateNewUserGame(NewUserGameBody body, String username, Game game) {
        User user = userService.findUser(username);
        if (user == null || game == null) throw new EntityNotFoundException();
        Set<String> allTags = new HashSet<>();
        allTags.addAll(game.getTags());
        allTags.addAll(body.getTags());

        return UserGame.builder()
                .user(user)
                .game(game)
                .timePlayed(body.getTimePlayed())
                .tags(allTags)
                .achivements(body.getAchivements())
                .totalAchivements(body.getTotalAchivements())
                .finished(body.getFinished())
                .favorite(body.getFavorite())
                .backlog(body.getBacklog())
                .build();
    }

    /**
     * Update a user game.
     *
     * @param body The body
     */
    private void updateUserGameContent(UpdateUserGameBody body, UserGame userGameToUpdate) {
        if (body.getTimePlayed() != null) {
            userGameToUpdate.setTimePlayed(body.getTimePlayed());
        }

        if (body.getTags() != null) {
            Set<String> newTags = body.getTags();
            userGameToUpdate.getTags().clear(); // Clear existing tags
            userGameToUpdate.getTags().addAll(newTags); // Add new tags
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
