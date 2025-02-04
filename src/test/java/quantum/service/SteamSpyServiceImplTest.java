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
import quantum.filter.AuthTokenFilter;
import quantum.service.impl.GameServiceImpl;
import quantum.service.impl.SteamGridDBServiceImpl;
import quantum.service.impl.SteamSpyServiceImpl;

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
class SteamSpyServiceImplTest {

    private SteamSpyServiceImpl steamSpyService;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private static MockWebServer mockWebServer;

    @BeforeEach
    void setup() throws IOException {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        this.steamSpyService = new SteamSpyServiceImpl(webClientBuilder);

        // Start the MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String mockServerUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        ReflectionTestUtils.setField(steamSpyService, "webClient", WebClient.builder().baseUrl(mockServerUrl).build());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Shut down the MockWebServer
        mockWebServer.shutdown();
    }

    /**
     * Test for RequiredArgsConstructor method.
     */
    @Test
    @DisplayName("Test RequiredArgsConstructor method (OK)")
    void testRequiredArgsConstructor() {
        SteamSpyServiceImpl service = new SteamSpyServiceImpl(webClient);
        assertNotNull(service);
    }

    /**
     * Test for {@link SteamSpyServiceImpl#getSteamSpyInfo} method.
     */
    @Test
    @DisplayName("Test getSteamSpyInfo method (OK)")
    void getSteamSpyInfoOK() {
        // Mock API response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"appid\":4000,\"name\":\"Garry's Mod\",\"developer\":\"Facepunch Studios\",\"publisher\":\"Valve\",\"score_rank\":\"\",\"positive\":1091540,\"negative\":36287,\"userscore\":0,\"owners\":\"20,000,000 .. 50,000,000\",\"average_forever\":13444,\"average_2weeks\":246,\"median_forever\":1679,\"median_2weeks\":86,\"price\":\"999\",\"initialprice\":\"999\",\"discount\":\"0\",\"ccu\":31594,\"languages\":\"English\",\"genre\":\"Casual, Indie, Simulation\",\"tags\":{\"Sandbox\":18633}}")

        );

        // Call the method and assert the response
        SteamSpyGame response = steamSpyService.getSteamSpyInfo(400L);
        assertNotNull(response);
    }

}
