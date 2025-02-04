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
import quantum.dto.steamSpy.SteamSpyGame;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.filter.AuthTokenFilter;
import quantum.service.impl.SteamServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test for {@link SteamServiceImpl} service class.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SteamServiceImplTest {

    private SteamServiceImpl steamService;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @Mock
    private SteamGridDBService steamGridDBService;

    @Mock
    private SteamSpyService steamSpyService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private static MockWebServer mockWebServer;

    private static final SteamSpyGame SAMPLE_STEAM_SPY_GAME = SteamSpyGame.builder()
            .appId(400L)
            .name("Portal")
            .tags(new HashMap<>())
            .build();

    private static final SGDBGame SAMPLE_SGDB_GAME = SGDBGame.builder()
            .id(9022L)
            .name("Portal")
            .releaseDate(1191999600L)
            .types(List.of("steam"))
            .verified(true)
            .build();

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

    @Test
    @DisplayName("Test RequiredArgsConstructor method (OK)")
    void testRequiredArgsConstructor() {
        SteamServiceImpl service = new SteamServiceImpl(steamGridDBService, steamSpyService, webClient);
        assertNotNull(service);
        ReflectionTestUtils.setField(service, "key", testKey);
    }

    /**
     * Test for {@link SteamServiceImpl#getUser} method.
     */
    @Test
    @DisplayName("Test getUser method (OK)")
    void testGetUser() {
        // Mock web server response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"response\": {\"players\": [] } }")
        );

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
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"response\":{\"game_count\":170,\"games\":[{\"appid\":4000,\"playtime_forever\":3770}]}}")
        );

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"response\":{\"game_count\":170,\"games\":[{\"appid\":4000,\"name\":\"Garry's Mod\",\"developer\":\"Facepunch Studios\",\"publisher\":\"Valve\",\"score_rank\":\"\",\"positive\":1091540,\"negative\":36287,\"userscore\":0,\"owners\":\"20,000,000 .. 50,000,000\",\"average_forever\":13444,\"average_2weeks\":246,\"median_forever\":1679,\"median_2weeks\":86,\"price\":\"999\",\"initialprice\":\"999\",\"discount\":\"0\",\"ccu\":31594,\"languages\":\"English\",\"genre\":\"Casual, Indie, Simulation\",\"tags\":{\"Sandbox\":18633,\"Moddable\":14430}]}}")

        );

        // Mock steam spy service
        when(steamSpyService.getSteamSpyInfo(any(Long.class))).thenReturn(SAMPLE_STEAM_SPY_GAME);

        // Mock sgdb service
        when(steamGridDBService.getBySteamId(any(Long.class))).thenReturn(SAMPLE_SGDB_GAME);
        when(steamGridDBService.getGridsById(any(Long.class))).thenReturn("{\"success\":true,\"data\":[{\"id\":103242,\"score\":0,\"style\":\"alternate\",\"width\":600,\"height\":900,\"nsfw\":false,\"humor\":false,\"notes\":null,\"mime\":\"image/png\",\"language\":\"en\",\"url\":\"https://cdn2.steamgriddb.com/grid/41a69c66f821f25c8184aea3bb35225d.png\",\"thumb\":\"https://cdn2.steamgriddb.com/thumb/41a69c66f821f25c8184aea3bb35225d.jpg\",\"lock\":false,\"epilepsy\":false,\"upvotes\":0,\"downvotes\":0,\"author\":{\"name\":\"Reiisen\",\"steam64\":\"76561198275966827\",\"avatar\":\"https://avatars.steamstatic.com/7e89e3dd262e818b545a5dc1a2f225f71984a9a0_medium.jpg\"}}]}");

        // Verify result
        UserGamesImportList response = steamService.getGames("76561198356072322");
        assertNotNull(response.getGames());
    }
}