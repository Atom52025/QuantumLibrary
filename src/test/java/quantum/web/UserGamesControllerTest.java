package quantum.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quantum.dto.userGames.*;
import quantum.dto.userGames.steamImport.UserGameImport;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.model.User;
import quantum.model.UserGame;
import quantum.service.UserGamesService;
import quantum.web.rest.UserGamesController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quantum.constant.TestConstants.SAMPLE_TOKEN;
import static quantum.constant.TestConstants.SAMPLE_USERNAME;
import static quantum.utils.TestUtils.stringifyObject;

/**
 * Test for {@link UserGamesController} controller class.
 */

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserGamesController.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserGamesControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserGamesService service;

    @BeforeEach
    void setUpSecurityContext() {
        UserDetails mockUser = new User(
                1L,
                "user",
                "email",
                "password",
                "role",
                "image",
                Collections.emptyList(),
                Collections.emptyList()
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * Test for {@link UserGamesController#getUserGames} method.
     *
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller GET")
    void getGames() throws Exception {

        when(service.getUserGames(any(String.class), any(String.class), any(Pageable.class))).thenReturn(new UserGamesListResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/games", SAMPLE_USERNAME)
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).getUserGames(any(String.class), any(String.class), any(Pageable.class));
    }

    /**
     * Test for {@link UserGamesController#getUserGames} method.
     *
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller GET (online)")
    void getOnlineGames() throws Exception {

        when(service.getOnlineGames(any(String.class))).thenReturn(new ArrayList<>());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/onlineGames", SAMPLE_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).getOnlineGames(any(String.class));
    }

    /**
     * Test for {@link UserGamesController#postUserGame} method.
     *
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller POST")
    void postGames() throws Exception {

        NewUserGameBody input = NewUserGameBody.builder()
                .name("name")
                .tags(Set.of("tags"))
                .image("image")
                .build();

        when(service.postUserGame(any(String.class), any(Long.class), any(NewUserGameBody.class))).thenReturn(new UserGameResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/games/{game_sgdb_id}", 1L)
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).postUserGame(any(String.class), any(Long.class), any(NewUserGameBody.class));
    }

    /**
     * Test for {@link UserGamesController#importUserGames} method.
     *
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller POST (import)")
    void importGames() throws Exception {

        UserGamesImportList input = UserGamesImportList.builder().games(
                Collections.nCopies(10,
                        UserGameImport.builder()
                                .name("name")
                                .image("image")
                                .timePlayed(1)
                                .sgdbId(1L)
                                .build())
        ).build();

        when(service.importUserGames(any(String.class), any(UserGamesImportList.class))).thenReturn(new UserGamesListResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/games/import", SAMPLE_USERNAME)
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).importUserGames(any(String.class), any(UserGamesImportList.class));
    }

    /**
     * Test for {@link UserGamesController#patchUserGame} method.
     *
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller PATCH")
    void patchGame() throws Exception {
        UpdateUserGameBody input = UpdateUserGameBody.builder().build();
        when(service.updateUserGame(any(String.class), any(Long.class), any(UpdateUserGameBody.class))).thenReturn(new UserGameResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/user/games/{game_sgdb_id}", 1L)
                .content(stringifyObject(input))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).updateUserGame(any(String.class), any(Long.class), any(UpdateUserGameBody.class));
    }

    /**
     * Test for {@link UserGamesController#deleteUserGame} method.
     *
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller DELETE")
    void deleteGame() throws Exception {

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/user/games/{game_sgdb_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        verify(service, times(1)).deleteUserGame(any(String.class), any(Long.class));
    }

    /**
     * Test for {@link UserGamesController#deleteUserGame} method.
     *
     * @throws Exception if any error occurs when performing the test request.
     */
    @Test
    @DisplayName("Test games controller GET (stats)")
    void getStats() throws Exception {
        when(service.getStats(any(String.class))).thenReturn(new StatsResponse());

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/games/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).getStats(any(String.class));
    }
}
