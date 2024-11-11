package quantum.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.service.SteamService;
import quantum.web.rest.SteamController;
import quantum.web.rest.SteamGridDBController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quantum.constant.TestConstants.SAMPLE_TOKEN;

/**
 * Test for {@link SteamController} controller class.
 */

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {SteamController.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SteamControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected SteamService service;

    @Test
    @DisplayName("Test steam controller GET user")
    void getUser() throws Exception {

        // Mock the service call
        when(service.getUser(anyString())).thenReturn("SampleUser");

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/steam/user/{steamId}", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect OK status
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Verify the service was called with the correct argument
        verify(service, times(1)).getUser(anyString());
    }

    @Test
    @DisplayName("Test steam controller GET games")
    void getGames() throws Exception {

        // Mock the service call
        UserGamesImportList gamesList = new UserGamesImportList();
        when(service.getGames(anyString())).thenReturn(gamesList);

        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/steam/games/{steamId}", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Perform the request and expect OK status
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Verify the service was called with the correct argument
        verify(service, times(1)).getGames(anyString());
    }
}