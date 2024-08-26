package quantum.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.service.impl.SteamServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SteamServiceImplTest {

    private SteamServiceImpl steamService;

    @Mock
    private SteamGridDBService steamGridDBService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private SteamSpyService steamSpyService;

    private static MockWebServer mockWebServer;

    @Value("${steam.api.key}")
    private String testKey;

    @BeforeEach
    void setup() throws IOException {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        this.steamService = new SteamServiceImpl(steamGridDBService, steamSpyService, webClientBuilder);

        // Start the MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String mockServerUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        ReflectionTestUtils.setField(steamService, "webClient", WebClient.builder().baseUrl(mockServerUrl).build());
        ReflectionTestUtils.setField(steamService, "key", testKey);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Shut Down the MockWebServer
        mockWebServer.shutdown();
    }

    /**
     * Test for {@link SteamServiceImpl#getUser} method.
     */
    @Test
    @DisplayName("Test getUser method (OK)")
    void testGetUser() {
        // Mock web server response
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"response\": {\"players\": [] } }"));

        // Verify result
        String response = steamService.getUser("test_steam_id");
        assertNotNull(response);
    }

    /**
     * Test for {@link SteamServiceImpl#getGames} method.
     */
    @Test
    @DisplayName("Test getGames method (OK)")
    void testGetGames() {
        // Mock web server response
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"response\":{\"game_count\":170,\"games\":[{\"appid\":4000,\"playtime_forever\":3770}]}}"));

        // Mock sgdb service
        when(steamGridDBService.getBySteamId(any(Long.class))).thenReturn("{\"success\":true,\"data\":{\"id\":9022,\"name\":\"Portal\",\"release_date\":1191999600,\"types\":[\"steam\"],\"verified\":true}}");
        when(steamGridDBService.getGridsById(any(Long.class))).thenReturn("{\"success\":true,\"data\":[{\"id\":103242,\"score\":0,\"style\":\"alternate\",\"width\":600,\"height\":900,\"nsfw\":false,\"humor\":false,\"notes\":null,\"mime\":\"image/png\",\"language\":\"en\",\"url\":\"https://cdn2.steamgriddb.com/grid/41a69c66f821f25c8184aea3bb35225d.png\",\"thumb\":\"https://cdn2.steamgriddb.com/thumb/41a69c66f821f25c8184aea3bb35225d.jpg\",\"lock\":false,\"epilepsy\":false,\"upvotes\":0,\"downvotes\":0,\"author\":{\"name\":\"Reiisen\",\"steam64\":\"76561198275966827\",\"avatar\":\"https://avatars.steamstatic.com/7e89e3dd262e818b545a5dc1a2f225f71984a9a0_medium.jpg\"}}]}");

        // Verify result
        UserGamesImportList response = steamService.getGames("76561198356072322");
        assertNotNull(response.getGames());
    }
}