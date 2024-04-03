package quantum.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.dto.sgdb.SGDBGameSuccessResponse;
import quantum.dto.sgdb.SGDBGrid;
import quantum.dto.sgdb.SGDBGridSuccessResponse;
import quantum.dto.steam.SteamResponse;
import quantum.dto.userGames.steamImport.UserGameImport;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.service.SteamGridDBService;
import quantum.service.SteamService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Steam API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SteamServiceImpl implements SteamService {

    private static final String EXTERNAL_API_URL = "http://api.steampowered.com/";
    private final SteamGridDBService steamGridBDService;
    private final WebClient webClient;

    @Value("${steam.api.key}")
    private String key;

    @Autowired
    public SteamServiceImpl(SteamGridDBService steamGridBDService, WebClient.Builder webClientBuilder) {
        this.steamGridBDService = steamGridBDService;
        this.webClient = webClientBuilder.baseUrl(EXTERNAL_API_URL).build();
    }

    /**
     * Get steam user.
     *
     * @param steamId The steam id to search for
     * @return The user found.
     */
    @Override
    public String getUser(String steamId) {
        String apiUrl = EXTERNAL_API_URL + "ISteamUser/GetPlayerSummaries/v0002/?key=" + key + "&steamids=" + steamId;
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
     *
     * @param steamId The steam id to search for
     * @return The games found.
     */
    @Override
    public UserGamesImportList getGames(String steamId) {
        String apiUrl = EXTERNAL_API_URL + "IPlayerService/GetOwnedGames/v0001/?key=" + key + "&steamid=" + steamId;
        String response = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<UserGameImport> userGames = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            SteamResponse steamResponse = objectMapper.readValue(response, SteamResponse.class);

            steamResponse.getResponse().getGames().stream().parallel().forEach(game -> {
                String gameResponse = steamGridBDService.getBySteamId(game.getAppId());
                if (gameResponse == null) {
                    log.error("Game not found in steam grid db");
                    return;
                }
                SGDBGameSuccessResponse sgdbResponse = null;
                try {
                    sgdbResponse = objectMapper.readValue(gameResponse, SGDBGameSuccessResponse.class);
                    String gridResponse = steamGridBDService.getGridsById(sgdbResponse.getData().getId());
                    SGDBGridSuccessResponse sgdbGridResponse = objectMapper.readValue(gridResponse, SGDBGridSuccessResponse.class);
                    String imageUrl = null;
                    if (!sgdbGridResponse.getData().isEmpty()) {
                        Optional<SGDBGrid> sgdbGrid = sgdbGridResponse.getData().stream().filter(e -> e.getWidth() == 600 && e.getHeight() == 900).findFirst();
                        imageUrl = sgdbGrid.orElse(sgdbGridResponse.getData().getFirst()).getUrl();
                    } else {
                        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/" + game.getAppId() + "/library_600x900.jpg";
                    }
                    UserGameImport newUserGame = UserGameImport.builder()
                            .name(sgdbResponse.getData().getName())
                            .timePlayed(game.getPlaytime())
                            .image(imageUrl)
                            .sgdbId(sgdbResponse.getData().getId())
                            .build();
                    userGames.add(newUserGame);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return UserGamesImportList.builder()
                .games(userGames)
                .build();
    }

}