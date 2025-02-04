package quantum.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.GameResponse;
import quantum.dto.userGames.*;
import quantum.dto.userGames.steamImport.UserGameImport;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.mapping.GamesMappingImpl;
import quantum.mapping.UserGamesMappingImpl;
import quantum.model.Game;
import quantum.model.User;
import quantum.model.UserGame;
import quantum.repository.UserGamesRepository;
import quantum.service.impl.UserGamesServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static quantum.constant.TestConstants.*;

/**
 * Test for {@link UserGamesServiceImpl} service class.
 */
@ExtendWith(MockitoExtension.class)
class UserGamesServiceImplTest {
    @Mock
    private UserGamesRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private GameService gameService;

    @Spy
    private UserGamesMappingImpl mapper;

    @Spy
    private GamesMappingImpl gamesMapper;

    @InjectMocks
    private UserGamesServiceImpl service;

    private static final NewUserGameBody SAMPLE_NEW_USER_GAME_BODY = NewUserGameBody.builder()
            .name("Test Game")
            .timePlayed(123)
            .image("GameImage")
            .defaultImage("DefaultImage")
            .tags(Set.of("GameTag1", "GameTag2"))
            .achivements(1)
            .totalAchivements(2)
            .finished(true)
            .favorite(true)
            .backlog(1)
            .build();

    private static final UpdateUserGameBody SAMPLE_UPDATE_USER_GAME_BODY = UpdateUserGameBody.builder()
            .achivements(1)
            .backlog(1)
            .favorite(true)
            .finished(true)
            .image("NewGameImage")
            .tags(Set.of("GameTag1", "GameTag2"))
            .timePlayed(123)
            .totalAchivements(2)
            .build();

    public static final GameResponse SAMPLE_GAME_RESPONSE = GameResponse.builder()
            .id(1L)
            .name("GameName")
            .tags(List.of("GameTag1", "GameTag2"))
            .image("GameImage")
            .sgdbId(1L)
            .build();

    public static final UserGameImport SAMPLE_USER_GAME_IMPORT = UserGameImport.builder()
            .sgdbId(5L)
            .name("ImportedGame")
            .timePlayed(500)
            .image("GameImage")
            .timePlayed(123)
            .tags(List.of("GameTag1", "GameTag2"))
            .build();

    /**
     * Test for {@link UserGamesServiceImpl#getUserGames} method.
     */
    @Test
    @DisplayName("Test getUserGames method (OK)")
    void getUserGamesOK() {
        List<UserGame> userGameList = List.of(SAMPLE_USER_GAME);
        Page<UserGame> userGamePage = new PageImpl<>(userGameList);

        // Mock dependencies
        when(repository.findByUser_Username(any(String.class), any(Pageable.class))).thenReturn(userGamePage);

        // Verify result
        UserGamesListResponse response = service.getUserGames(SAMPLE_USERNAME, "all", Pageable.unpaged());

        assertEquals(userGameList.size(), response.getGames().size());
        assertEquals(SAMPLE_USER_GAME.getGame(), response.getGames().getFirst().getGame());
        assertEquals(SAMPLE_USER_GAME.getTimePlayed(), response.getGames().getFirst().getTimePlayed());
        assertEquals(SAMPLE_USER_GAME.getImage(), response.getGames().getFirst().getImage());
    }

    /**
     * Test for {@link UserGamesServiceImpl#getOnlineGames} method.
     */
    @Test
    @DisplayName("Test getOnlineGames method (OK)")
    void getOnlineGamesOK() {
        List<UserGame> userGameList = List.of(SAMPLE_USER_GAME);

        // Mock dependencies
        when(repository.findByUser_UsernameAndTagsIn(any(String.class), any(Set.class))).thenReturn(userGameList);

        // verify result
        List<UserGame> result = service.getOnlineGames(SAMPLE_USERNAME);

        assertEquals(userGameList, result);
    }

    /**
     * Test for {@link UserGamesServiceImpl#getCommonGames} method.
     */
    @Test
    @DisplayName("Test getCommonGames method (OK)")
    void getCommonGamesOK() {
        List<User> users = List.of(User.builder().username("user1").build(), User.builder().username("user2").build());

        // Mock dependencies
        when(repository.findByUser_UsernameAndTagsIn(any(String.class), any(Set.class))).thenReturn(List.of(SAMPLE_USER_GAME));

        // Verify result
        Set<Game> result = service.getCommonGames(users);

        assertThat(result).hasSize(1).extracting(Game::getName).containsExactly(SAMPLE_USER_GAME.getGame().getName());
    }

    /**
     * Test for {@link UserGamesServiceImpl#postUserGame} method.
     */
    @Test
    @DisplayName("Test postUserGame method (OK)")
    void postUserGameOK() {
        // Clone the entity so the test doesn't modify the original object
        User user = new User(SAMPLE_USER.getId(), SAMPLE_USER.getUsername(), SAMPLE_USER.getEmail(), SAMPLE_USER.getPassword(), SAMPLE_USER.getRole(), SAMPLE_USER.getImage(), SAMPLE_USER.getUserGames(), SAMPLE_USER.getUserGroups());

        // Mock dependencies
        when(repository.findByUser_UsernameAndGame_SgdbId(any(String.class), any(Long.class))).thenReturn(Optional.empty());
        when(gameService.findGameById(any(Long.class), any(Boolean.class))).thenReturn(SAMPLE_GAME);
        when(userService.findUser(SAMPLE_USERNAME)).thenReturn(user);
        when(repository.save(any(UserGame.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Verify result
        UserGameResponse response = service.postUserGame(SAMPLE_USERNAME, 1L, SAMPLE_NEW_USER_GAME_BODY);

        assertEquals(SAMPLE_NEW_USER_GAME_BODY.getTimePlayed(), response.getTimePlayed());
        assertEquals(SAMPLE_NEW_USER_GAME_BODY.getImage(), response.getImage());
        assertEquals(SAMPLE_NEW_USER_GAME_BODY.getAchivements(), response.getAchivements());
        assertEquals(SAMPLE_NEW_USER_GAME_BODY.getTotalAchivements(), response.getTotalAchivements());
    }

    /**
     * Test for {@link UserGamesServiceImpl#updateUserGame} method.
     */
    @Test
    @DisplayName("Test updateUserGame method (OK)")
    void updateUserGameOK() {
        // Clone the entity so the test doesn't modify the original object
        UserGame userGameToUpdate = new UserGame(SAMPLE_USER_GAME.getId(), SAMPLE_USER_GAME.getUser(), SAMPLE_USER_GAME.getGame(), SAMPLE_USER_GAME.getTimePlayed(), new HashSet<>(SAMPLE_USER_GAME.getTags()), SAMPLE_USER_GAME.getImage(), SAMPLE_USER_GAME.getAchivements(), SAMPLE_USER_GAME.getTotalAchivements(), SAMPLE_USER_GAME.getFinished(), SAMPLE_USER_GAME.getFavorite(), SAMPLE_USER_GAME.getBacklog());

        // Mock dependencies
        when(repository.findByUser_UsernameAndGame_Id(any(String.class), any(Long.class))).thenReturn(Optional.of(userGameToUpdate));
        when(repository.save(any(UserGame.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Verify result
        UserGameResponse response = service.updateUserGame(SAMPLE_USERNAME, SAMPLE_USER_GAME.getId(), SAMPLE_UPDATE_USER_GAME_BODY);

        assertEquals(SAMPLE_UPDATE_USER_GAME_BODY.getTimePlayed(), response.getTimePlayed());
        assertEquals(SAMPLE_UPDATE_USER_GAME_BODY.getImage(), response.getImage());
        assertEquals(SAMPLE_UPDATE_USER_GAME_BODY.getAchivements(), response.getAchivements());
        assertEquals(SAMPLE_UPDATE_USER_GAME_BODY.getTotalAchivements(), response.getTotalAchivements());
    }

    /**
     * Test for {@link UserGamesServiceImpl#deleteUserGame} method.
     */
    @Test
    @DisplayName("Test deleteUserGame method (OK)")
    void deleteUserGameOK() {
        // Clone the entity so the test doesn't modify the original object
        UserGame userGame = new UserGame(SAMPLE_USER_GAME.getId(), SAMPLE_USER_GAME.getUser(), SAMPLE_USER_GAME.getGame(), SAMPLE_USER_GAME.getTimePlayed(), SAMPLE_USER_GAME.getTags(), SAMPLE_USER_GAME.getImage(), SAMPLE_USER_GAME.getAchivements(), SAMPLE_USER_GAME.getTotalAchivements(), SAMPLE_USER_GAME.getFinished(), SAMPLE_USER_GAME.getFavorite(), SAMPLE_USER_GAME.getBacklog());

        // Mock dependencies
        when(repository.findByUser_UsernameAndGame_Id(any(String.class), any(Long.class))).thenReturn(Optional.of(userGame));

        // Verify result
        assertDoesNotThrow(() -> service.deleteUserGame(SAMPLE_USER.getUsername(), SAMPLE_GAME.getId()));
    }

    /**
     * Test for {@link UserGamesServiceImpl#getStats} method.
     */
    @Test
    @DisplayName("Test getStats method (OK)")
    void getStatsOK() {
        // Mock dependencies
        when(repository.findByUser_UsernameAndBacklog(any(String.class), any(Integer.class), any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(repository.findByUser_Username(any(String.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(SAMPLE_USER_GAME)));

        // Verify result
        StatsResponse stats = service.getStats(SAMPLE_USERNAME);

        assertEquals(1, stats.getNumOfFinishedGames());
    }

    /**
     * Test for {@link UserGamesServiceImpl#importUserGames} method.
     */
    @Test
    @DisplayName("Test importUserGames method (OK)")
    void importUserGamesOK() {
        UserGamesImportList userGamesImportList = UserGamesImportList.builder()
                .games(List.of(SAMPLE_USER_GAME_IMPORT)).build();

        // Mock dependencies
        when(gameService.getGames(any(Pageable.class))).thenReturn(GameListResponse.builder().games(List.of(SAMPLE_GAME_RESPONSE)).build());
        when(repository.findByUser_Username(any(String.class), any(Pageable.class))).thenReturn(Page.empty());
        when(userService.findUser(any(String.class))).thenReturn(SAMPLE_USER);
        when(repository.saveAll(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Verify result
        UserGamesListResponse response = service.importUserGames(SAMPLE_USERNAME, userGamesImportList);

        assertEquals(SAMPLE_USER_GAME_IMPORT.getName(), response.getGames().getFirst().getGame().getName());
        assertEquals(SAMPLE_USER_GAME_IMPORT.getTimePlayed(), response.getGames().getFirst().getTimePlayed());
        assertEquals(SAMPLE_USER_GAME_IMPORT.getImage(), response.getGames().getFirst().getImage());
    }
}
