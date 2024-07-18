package quantum.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quantum.dto.game.GameListResponse;
import quantum.dto.game.GameResponse;
import quantum.dto.game.NewGameBody;
import quantum.dto.game.UpdateGameBody;
import quantum.service.GameService;
import quantum.web.rest.GameController;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quantum.util.TestUtils.stringifyObject;

/**
 * Test for {@link GameController} controller class.
 */

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {GameController.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class GameControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected GameService service;

    /**
     * Test for {@link GameController#getGames} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller GET")
    void getGames() throws Exception {

        when(service.getGames(any(Pageable.class))).thenReturn(new GameListResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/games")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).getGames(any(Pageable.class));
    }

    /**
     * Test for {@link GameController#postGame} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller POST")
    void postGames() throws Exception {

        NewGameBody input = NewGameBody.builder()
                .name("name")
                .tags(Set.of("tags"))
                .sgdbId(1L)
                .image("image")
                .build();

        when(service.postGame(any(NewGameBody.class), any(Boolean.class))).thenReturn(new GameResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/games")
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).postGame(any(NewGameBody.class), any(Boolean.class));
    }

    /**
     * Test for {@link GameController#patchGame} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller PATCH")
    void patchGame() throws Exception {

        UpdateGameBody input = UpdateGameBody.builder()
                .name("name")
                .tags(Set.of("tags"))
                .image("image")
                .build();

        when(service.updateGame(any(Long.class), any(UpdateGameBody.class))).thenReturn(new GameResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/games/{gameId}", 1L)
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).updateGame(any(Long.class), any(UpdateGameBody.class));
    }

    /**
     * Test for {@link GameController#deleteGame} method.
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller DELETE")
    void deleteGame() throws Exception {

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/games/{gameId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).deleteGame(any(Long.class));
    }
}
