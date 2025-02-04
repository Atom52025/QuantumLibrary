package quantum.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.dto.sgdb.SGDBGame;
import quantum.filter.AuthTokenFilter;
import quantum.service.impl.SteamGridDBServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Test for {@link SteamGridDBServiceImpl} service class.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SteamGridDBServiceImplTest {

    private SteamGridDBServiceImpl steamGridDBService;

    @MockBean
    private AuthTokenFilter authTokenFilter;

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
    @DisplayName("Test RequiredArgsConstructor method (OK)")
    void testRequiredArgsConstructor() {
        SteamGridDBServiceImpl service = new SteamGridDBServiceImpl(webClient);
        assertNotNull(service);
        ReflectionTestUtils.setField(service, "key", testKey);
    }

    @Test
    @DisplayName("Test searchByTerm method (OK)")
    void searchByTermOK() {
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
    @DisplayName("Test getBySteamId method (OK)")
    void getBySteamIdOK() {
        // Mock API response for getBySteamId
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"steam_id\": 76561198034336239, \"name\": \"Test Steam Game\"}")
        );

        // Call the method and assert the response
        SGDBGame response = steamGridDBService.getBySteamId(76561198034336239L);
        assertNull(response);
    }

    @Test
    @DisplayName("Test getBySteamId method (Not Found)")
    void getBySteamIdNotFound() {
        // Mock 404 Not Found response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("")
        );

        // Call the method and assert the response is null
        SGDBGame response = steamGridDBService.getBySteamId(76561198034336239L);
        assertNull(response);
    }

    @Test
    @DisplayName("Test getGridsById method (OK)")
    void getGridsByIdOK() {
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
