package quantum.service;

import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.GameResponse;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;
import quantum.mapping.GamesMappingImpl;
import quantum.model.Game;
import quantum.repository.GameRepository;
import quantum.service.impl.GameServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static quantum.constant.TestConstants.SAMPLE_GAME;

/**
 * Test for {@link GameServiceImpl} service class.
 */
@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Spy
    private GamesMappingImpl mapper;

    @InjectMocks
    private GameServiceImpl service;

    private static final NewGameBody SAMPLE_NEW_GAME_BODY = new NewGameBody(
            SAMPLE_GAME.getName(),
            SAMPLE_GAME.getImage(),
            SAMPLE_GAME.getTags(),
            SAMPLE_GAME.getSgdbId()
    );

    private static final UpdateGameBody SAMPLE_UPDATE_GAME_BODY = UpdateGameBody.builder()
            .name("GameNameUpdated")
            .tags(Set.of("Tag1Updated", "Tag2Updated"))
            .image("ImageUpdated")
            .build();

    private static final Game SAMPLE_UPDATE_GAME = Game.builder()
            .id(1L)
            .name("GameNameUpdated")
            .tags(Set.of("Tag1Updated", "Tag2Updated"))
            .sgdbId(1L)
            .image("ImageUpdated")
            .build();

    /**
     * Test for {@link GameServiceImpl#getGames} method.
     */
    @Test
    @DisplayName("Test getGames method (OK)")
    void getGamesOK() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Game> resultContent = Collections.nCopies(10, SAMPLE_GAME);
        Page<Game> pagedResult = new PageImpl<>(resultContent, pageable, resultContent.size());

        // Mock repository
        when(gameRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);
        when(mapper.map(any(Game.class))).thenCallRealMethod();

        GameListResponse response = service.getGames(pageable);

        // Verify result
        assertEquals(10, response.getGames().size());
        assertEquals(SAMPLE_GAME.getId(), response.getGames().get(0).getId());
        assertEquals(SAMPLE_GAME.getName(), response.getGames().get(0).getName());
        assertEquals(SAMPLE_GAME.getImage(), response.getGames().get(0).getImage());
        assertEquals(SAMPLE_GAME.getSgdbId(), response.getGames().get(0).getSgdbId());
        assertEquals(SAMPLE_GAME.getTags().stream().toList(), response.getGames().get(0).getTags());
    }

    /**
     * Test for {@link GameServiceImpl#getGames} method.
     */
    @Test
    @DisplayName("Test getGames method (DatabaseConnectionException)")
    void getGamesDatabaseConnectionException() {
        Pageable pageable = PageRequest.of(0, 10);

        // Mock repository
        when(gameRepository.findAll(any(Pageable.class))).thenThrow(JDBCConnectionException.class);

        // Verify result
        assertThrows(DatabaseConnectionException.class, () -> service.getGames(pageable));
    }

    /**
     * Test for {@link GameServiceImpl#getGames} method.
     */
    @Test
    @DisplayName("Test getGames method (EntityNotFoundException)")
    void getGamesEntityNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Game> resultContent = Collections.nCopies(0, SAMPLE_GAME);
        Page<Game> pagedResult = new PageImpl<>(resultContent, pageable, resultContent.size());

        // Mock repository
        when(gameRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

        // Verify result
        assertThrows(EntityNotFoundException.class, () -> service.getGames(pageable));
    }

    /**
     * Test for {@link GameServiceImpl#findGameById} method.
     */
    @Test
    @DisplayName("Test findGameById method (OK)")
    void findGameByIdOK() {
        Optional<Game> resultContent = Optional.of(SAMPLE_GAME);

        // Mock repository (Id)
        when(gameRepository.findById(any(Long.class))).thenReturn(resultContent);
        when(gameRepository.findBySgdbId(any(Long.class))).thenReturn(resultContent);

        // Verify result (Id)
        Game response = service.findGameById(SAMPLE_GAME.getId(), false);
        assertEquals(SAMPLE_GAME.getId(), response.getId());
        assertEquals(SAMPLE_GAME.getName(), response.getName());
        assertEquals(SAMPLE_GAME.getImage(), response.getImage());
        assertEquals(SAMPLE_GAME.getSgdbId(), response.getSgdbId());
        assertEquals(SAMPLE_GAME.getTags(), response.getTags());

        // Mock repository (SgdbId)
        when(gameRepository.findBySgdbId(any(Long.class))).thenReturn(resultContent);

        // Verify result (SgdbId)
        Game sgdbResponse = service.findGameById(SAMPLE_GAME.getId(), true);

        assertEquals(SAMPLE_GAME.getId(), sgdbResponse.getId());
        assertEquals(SAMPLE_GAME.getName(), sgdbResponse.getName());
        assertEquals(SAMPLE_GAME.getImage(), sgdbResponse.getImage());
        assertEquals(SAMPLE_GAME.getSgdbId(), sgdbResponse.getSgdbId());
        assertEquals(SAMPLE_GAME.getTags(), sgdbResponse.getTags());
    }

    /**
     * Test for {@link GameServiceImpl#findGameById} method.
     */
    @Test
    @DisplayName("Test findGameById method (DatabaseConnectionException)")
    void findGameByIdDatabaseConnectionException() {
        // Mock repository
        when(gameRepository.findById(any(Long.class))).thenThrow(JDBCConnectionException.class);
        when(gameRepository.findBySgdbId(any(Long.class))).thenThrow(JDBCConnectionException.class);

        // Verify result
        assertThrows(DatabaseConnectionException.class, () -> service.findGameById(SAMPLE_GAME.getId(), false));
        assertThrows(DatabaseConnectionException.class, () -> service.findGameById(SAMPLE_GAME.getId(), true));
    }

    /**
     * Test for {@link GameServiceImpl#findGameById} method.
     */
    @Test
    @DisplayName("Test findGameById method (EntityNotFoundException)")
    void findGameByIdEntityNotFoundException() {
        // Mock repository
        when(gameRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(gameRepository.findBySgdbId(any(Long.class))).thenReturn(Optional.empty());

        // Verify result
        assertThrows(EntityNotFoundException.class, () -> service.findGameById(SAMPLE_GAME.getId(), false));
        assertDoesNotThrow(() -> service.findGameById(SAMPLE_GAME.getId(), true));
    }

    /**
     * Test for {@link GameServiceImpl#postGame} method.
     */
    @Test
    @DisplayName("Test postGame method (OK)")
    void postGameOK() {
        // Mock repository
        when(gameRepository.save(any(Game.class))).then(element -> element.getArgument(0));
        when(mapper.map(any(Game.class))).thenCallRealMethod();

        // Verify result
        GameResponse response = (GameResponse) service.postGame(SAMPLE_NEW_GAME_BODY, false);

        assertEquals(SAMPLE_GAME.getName(), response.getName());
        assertEquals(SAMPLE_GAME.getImage(), response.getImage());
        assertEquals(SAMPLE_GAME.getSgdbId(), response.getSgdbId());
        assertEquals(SAMPLE_GAME.getTags().stream().toList(), response.getTags());
    }

    /**
     * Test for {@link GameServiceImpl#postGame} method.
     */
    @Test
    @DisplayName("Test postGame method (Error on save)")
    void postGameDatabaseConnectionException() {
        // Mock repository (DatabaseConnectionException)
        when(gameRepository.save(any(Game.class))).thenThrow(DataIntegrityViolationException.class);

        // Verify result (DatabaseConnectionException)
        assertThrows(DatabaseConnectionException.class, () -> service.postGame(SAMPLE_NEW_GAME_BODY, false));

        // Mock repository (JDBCConnectionException)
        when(gameRepository.save(any(Game.class))).thenThrow(JDBCConnectionException.class);

        // Verify result (JDBCConnectionException)
        assertThrows(DatabaseConnectionException.class, () -> service.postGame(SAMPLE_NEW_GAME_BODY, false));
    }

    /**
     * Test for {@link GameServiceImpl#updateGame} method.
     */
    @Test
    @DisplayName("Test updateGame method (OK)")
    void updateGameOK() {
        // Clone the game so the test doesn't modify the original object
        Game testGame = new Game(SAMPLE_GAME.getId(), SAMPLE_GAME.getName(), SAMPLE_GAME.getImage(), SAMPLE_GAME.getTags(), SAMPLE_GAME.getSgdbId(), SAMPLE_GAME.getUserGames());

        // Mock repository
        when(gameRepository.findById(any(Long.class))).thenReturn(Optional.of(testGame));
        when(gameRepository.save(any(Game.class))).then(element -> element.getArgument(0));

        // Verify result
        GameResponse response = service.updateGame(SAMPLE_GAME.getId(), SAMPLE_UPDATE_GAME_BODY);

        assertEquals(SAMPLE_UPDATE_GAME.getId(), response.getId());
        assertEquals(SAMPLE_UPDATE_GAME.getName(), response.getName());
        assertEquals(SAMPLE_UPDATE_GAME.getImage(), response.getImage());
        assertEquals(SAMPLE_UPDATE_GAME.getSgdbId(), response.getSgdbId());
        assertEquals(SAMPLE_UPDATE_GAME.getTags().stream().toList(), response.getTags());
    }

    /**
     * Test for {@link GameServiceImpl#updateGame} method.
     */
    @Test
    @DisplayName("Test updateGame method (DatabaseConnectionException)")
    void updateGameDatabaseConnectionException() {
        // Mock repository
        when(gameRepository.findById(any(Long.class))).thenReturn(Optional.of(SAMPLE_GAME));
        doThrow(JDBCConnectionException.class).when(gameRepository).save(any(Game.class));

        // Verify result
        assertThrows(DatabaseConnectionException.class, () -> service.updateGame(SAMPLE_GAME.getId(), SAMPLE_UPDATE_GAME_BODY));
    }

    /**
     * Test for {@link GameServiceImpl#updateGame} method.
     */
    @Test
    @DisplayName("Test updateGame method (EntityNotFoundException)")
    void updateGameEntityNotFoundException() {
        // Mock repository
        when(gameRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Verify result
        assertThrows(EntityNotFoundException.class, () -> service.updateGame(SAMPLE_GAME.getId(), SAMPLE_UPDATE_GAME_BODY));
    }

    /**
     * Test for {@link GameServiceImpl#deleteGame} method.
     */
    @Test
    @DisplayName("Test deleteGame method (OK)")
    void deleteGameOK() {
        // Clone the game so the test doesn't modify the original object
        Game testGame = new Game(SAMPLE_GAME.getId(), SAMPLE_GAME.getName(), SAMPLE_GAME.getImage(), SAMPLE_GAME.getTags(), SAMPLE_GAME.getSgdbId(), SAMPLE_GAME.getUserGames());

        // Mock repository
        when(gameRepository.findById(any(Long.class))).thenReturn(Optional.of(testGame));

        // Verify result
        assertDoesNotThrow(() -> service.deleteGame(SAMPLE_GAME.getId()));
    }

    /**
     * Test for {@link GameServiceImpl#deleteGame} method.
     */
    @Test
    @DisplayName("Test deleteGame method (DatabaseConnectionException)")
    void deleteGameDatabaseConnectionException() {
        // Mock repository
        when(gameRepository.findById(any(Long.class))).thenReturn(Optional.of(SAMPLE_GAME));
        doThrow(JDBCConnectionException.class).when(gameRepository).delete(any(Game.class));

        // Verify result
        assertThrows(DatabaseConnectionException.class, () -> service.deleteGame(SAMPLE_GAME.getId()));
    }
    
}
