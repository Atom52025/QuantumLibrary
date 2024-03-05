package quantum.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.dto.sgdb.SGBDGameSuccessResponse;
import quantum.dto.sgdb.SGBDGridSuccessResponse;
import quantum.dto.steam.SteamGame;
import quantum.dto.steam.SteamResponse;
import quantum.dto.usergames.UserGameImportResponse;
import quantum.dto.usergames.UserGamesImportListResponse;
import quantum.service.SteamGridBDService;
import quantum.service.SteamService;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for Steam API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SteamServiceImpl implements SteamService {

    private final SteamGridBDService steamGridBDService;

    private final WebClient webClient;
    private static final String EXTERNAL_API_URL = "http://api.steampowered.com/";
    private static final String KEY = "44F5764DDFE092DDC655B6CB72D65170";

    @Autowired
    public SteamServiceImpl(SteamGridBDService steamGridBDService, WebClient.Builder webClientBuilder) {
        this.steamGridBDService = steamGridBDService;
        this.webClient = webClientBuilder.baseUrl(EXTERNAL_API_URL).build();
    }

    /**
     * Get steam user.
     * @param steamId The steam id to search for
     * @return The user found.
     */
    @Override
    public String getUser(String steamId) {
        String apiUrl = EXTERNAL_API_URL + "ISteamUser/GetPlayerSummaries/v0002/?key=" + KEY + "&steamids=" + steamId;
        String response = webClient.get()
                            .uri(apiUrl)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
        // Return empty array if response is null
        if (response == null) {
            log.error("Error getting response from steam");
            return "{\"response\": {\"players\": [] } }";
        }
        return response;
    }

    /**
     * Get steam games.
     * @param steamId The steam id to search for
     * @return The games found.
     */
    @Override
    public UserGamesImportListResponse getGames(String steamId) {
        String apiUrl = EXTERNAL_API_URL + "IPlayerService/GetOwnedGames/v0001/?key=" + KEY + "&steamid=" + steamId;
        String response = webClient.get()
                                .uri(apiUrl)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

        List<UserGameImportResponse> userGames = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            SteamResponse steamResponse = objectMapper.readValue(response, SteamResponse.class);

            for (SteamGame game : steamResponse.getResponse().getGames()) {
                String gameResponse = steamGridBDService.getBySteamId(game.getAppId());
                if (gameResponse == null) {
                    log.error("Game not found in steam grid db");
                    continue;
                }
                SGBDGameSuccessResponse sgbdResponse = objectMapper.readValue(gameResponse, SGBDGameSuccessResponse.class);
                String gridResponse = steamGridBDService.getGridsById(sgbdResponse.getData().getId());
                SGBDGridSuccessResponse sgdbGridResponse = objectMapper.readValue(gridResponse, SGBDGridSuccessResponse.class);
                String imageUrl = null;
                if (!sgdbGridResponse.getData().isEmpty()) {
                    imageUrl = sgdbGridResponse.getData().get(0).getUrl();
                }
                UserGameImportResponse newUserGame = UserGameImportResponse.builder()
                        .name(sgbdResponse.getData().getName())
                        .timePlayed(game.getPlaytime())
                        .image(imageUrl)
                        .sgbdId(sgbdResponse.getData().getId())
                        .build();
                userGames.add(newUserGame);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return UserGamesImportListResponse.builder()
                .games(userGames)
                .build();
    }
}