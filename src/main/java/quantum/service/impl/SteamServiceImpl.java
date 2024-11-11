package quantum.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.dto.sgdb.SGDBGame;
import quantum.dto.sgdb.SGDBGameSuccessResponse;
import quantum.dto.sgdb.SGDBGrid;
import quantum.dto.sgdb.SGDBGridSuccessResponse;
import quantum.dto.steam.SteamResponse;
import quantum.dto.steamSpy.SteamSpyGame;
import quantum.dto.userGames.steamImport.UserGameImport;
import quantum.dto.userGames.steamImport.UserGamesImportList;
import quantum.exceptions.QuantumLibraryGenericException;
import quantum.service.SteamGridDBService;
import quantum.service.SteamService;
import quantum.service.SteamSpyService;

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
    private final SteamSpyService steamSpyService;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${steam.api.key}")
    private String key;

    @Autowired
    public SteamServiceImpl(SteamGridDBService steamGridBDService, SteamSpyService steamSpyService, WebClient.Builder webClientBuilder) {
        this.steamGridBDService = steamGridBDService;
        this.steamSpyService = steamSpyService;
        this.webClient = webClientBuilder.baseUrl(EXTERNAL_API_URL).build();
    }

    //------------------------------------- PUBLIC METHODS -------------------------------------//

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

            SteamResponse steamResponse = objectMapper.readValue(response, SteamResponse.class);

            steamResponse.getResponse().getGames().stream().parallel().forEach(game -> {
                SteamSpyGame steamSpyGame = steamSpyService.getSteamSpyInfo(game.getAppId());
                if (steamSpyGame == null) {
                    return;
                }
                SGDBGame sgdbGame = getSGDBInfo(game.getAppId());
                String imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/" + game.getAppId() + "/library_600x900.jpg";
                if (sgdbGame != null) {
                    List<SGDBGrid> sgdbGrids = getSGDBGrids(sgdbGame.getId());
                    if (!sgdbGrids.isEmpty()) {
                        Optional<SGDBGrid> sgdbGrid = sgdbGrids.stream().filter(e -> e.getWidth() == 600 && e.getHeight() == 900).findFirst();
                        imageUrl = sgdbGrid.orElse(sgdbGrids.getFirst()).getUrl();
                    }
                }
                assert sgdbGame != null;
                UserGameImport newUserGame = UserGameImport.builder()
                        .name(steamSpyGame.getName())
                        .timePlayed(game.getPlaytime())
                        .image(imageUrl)
                        .sgdbId(sgdbGame.getId())
                        .tags(steamSpyGame.getTags().keySet().stream().toList())
                        .build();
                userGames.add(newUserGame);
            });

        } catch (JsonProcessingException e) {
            throw new QuantumLibraryGenericException("Error parsing response from steam", e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }

        return UserGamesImportList.builder()
                .games(userGames)
                .build();
    }

    //------------------------------------- PRIVATE METHODS -------------------------------------//

    /**
     * Get steam grid db grids.
     *
     * @param sgbdId The sgbd id to search for
     * @return The info found.
     */
    private SGDBGame getSGDBInfo(Long sgbdId) {
        try {
            String gameResponse = steamGridBDService.getBySteamId(sgbdId);
            return objectMapper.readValue(gameResponse, SGDBGameSuccessResponse.class).getData();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Get steam grid db info.
     *
     * @param steamId The steam id to search for
     * @return The info found.
     */
    private List<SGDBGrid> getSGDBGrids(Long steamId) {
        try {
            String gridResponse = steamGridBDService.getGridsById(steamId);
            return objectMapper.readValue(gridResponse, SGDBGridSuccessResponse.class).getData();
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

}