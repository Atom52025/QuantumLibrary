package quantum.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.service.impl.SteamGridDBServiceImpl;
import quantum.service.impl.SteamServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SteamGridDBServiceImplTest {

    private SteamGridDBServiceImpl steamGridDBService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private static MockWebServer mockWebServer;

    @Value("${steamdb.api.key}")
    private String testKey;

    @BeforeEach
    void setup() throws IOException {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        this.steamGridDBService = new SteamGridDBServiceImpl(webClientBuilder);

        // Start the MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String mockServerUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        ReflectionTestUtils.setField(steamGridDBService, "webClient", WebClient.builder().baseUrl(mockServerUrl).build());
        ReflectionTestUtils.setField(steamGridDBService, "key", testKey);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Shut down the MockWebServer
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Test searchByTerm method (Success)")
    void searchByTerm_success() {
        // Mock API response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"data\": [\"Game1\", \"Game2\"]}")
        );

        // Call the method and assert the response
        String response = steamGridDBService.searchByTerm("TestGame");
        assertNotNull(response);
    }

    @Test
    @DisplayName("Test searchByTerm method (Empty response)")
    void searchByTerm_emptyResponse() {
        // Mock empty response from API
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(""));

        // Call the method and assert the response is an empty array
        String response = steamGridDBService.searchByTerm("TestGame");
        assertNotNull(response);
    }

    @Test
    @DisplayName("Test getById method (Success)")
    void getById_success() {
        // Mock API response for getById
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\": 123, \"name\": \"Test Game\"}")
        );

        // Call the method and assert the response
        String response = steamGridDBService.getById(123L);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Test getBySteamId method (Success)")
    void getBySteamId_success() {
        // Mock API response for getBySteamId
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"steam_id\": 76561198034336239, \"name\": \"Test Steam Game\"}")
        );

        // Call the method and assert the response
        String response = steamGridDBService.getBySteamId(76561198034336239L);
        assertNull(response);
    }

    @Test
    @DisplayName("Test getBySteamId method (Not Found)")
    void getBySteamId_notFound() {
        // Mock 404 Not Found response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("")
        );

        // Call the method and assert the response is null
        String response = steamGridDBService.getBySteamId(76561198034336239L);
        assertNull(response);
    }

    @Test
    @DisplayName("Test getGridsById method (Success)")
    void getGridsById_success() {
        // Mock API response for getGridsById
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"grids\": [\"Grid1\", \"Grid2\"]}")
        );

        // Call the method and assert the response
        String response = steamGridDBService.getGridsById(456L);
        assertNotNull(response);
    }
}
