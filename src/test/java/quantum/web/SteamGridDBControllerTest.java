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
import quantum.service.SteamGridDBService;
import quantum.web.rest.AuthController;
import quantum.web.rest.SteamGridDBController;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quantum.constant.TestConstants.SAMPLE_TOKEN;

/**
 * Test for {@link SteamGridDBController} controller class.
 */

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {SteamGridDBController.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SteamGridDBControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected SteamGridDBService service;

    @Test
    @DisplayName("Test SteamGridDB controller GET search by term")
    void searchByTerm() throws Exception {
        // Arrange
        when(service.searchByTerm(anyString())).thenReturn("SampleResponse");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/sgdb/search")
                .param("term", "sample")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Act & Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Verify
        verify(service, times(1)).searchByTerm(anyString());
    }

    @Test
    @DisplayName("Test SteamGridDB controller GET grids by game ID")
    void getGrids() throws Exception {
        // Arrange
        when(service.getGridsById(anyLong())).thenReturn("SampleGridResponse");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/sgdb/getGrids/{game_sgdb_id}", 1L)
                .param("gameSgdbId", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SAMPLE_TOKEN);

        // Act & Assert
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Verify
        verify(service, times(1)).getGridsById(anyLong());
    }
}
