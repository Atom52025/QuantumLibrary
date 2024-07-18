package quantum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import quantum.service.SteamGridDBService;

/**
 * Service implementation for Steam Grid DB API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SteamGridDBServiceImpl implements SteamGridDBService {


    private static final String EXTERNAL_API_URL = "https://www.steamgriddb.com/api/v2/";
    private static final String AUTH_TOKEN = "1a8c79dc9eff0c11128e1a230c8abdae";
    private final WebClient webClient;

    @Autowired
    public SteamGridDBServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(EXTERNAL_API_URL).build();
    }

    //------------------------------------- PUBLIC METHODS -------------------------------------//

    /**
     * Search game in steam grid db by term.
     *
     * @param term The term to search for
     * @return The games found.
     */
    @Override
    public String searchByTerm(String term) {
        String apiUrl = EXTERNAL_API_URL + "search/autocomplete/" + term;
        String response = webClient.get()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        // Return empty array if response is null
        if (response == null) {
            log.error("Error getting response from steam grid db");
            return "{\"data\":[]}";
        }
        return response;
    }

    /**
     * Get game in steam grid db by id.
     *
     * @param id The id to search for
     * @return The game found.
     */
    @Override
    public String getById(Long id) {
        String apiUrl = EXTERNAL_API_URL + "games/id/" + id;
        return webClient.get()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Get game in steam grid db by id.
     *
     * @param id The id to search for
     * @return The game found.
     */
    @Override
    public String getBySteamId(Long id) {
        String apiUrl = EXTERNAL_API_URL + "games/steam/" + id;
        try {
            return webClient.get()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + AUTH_TOKEN)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            return null;
        }
    }

    /**
     * Get game grids in steam grid db by id.
     *
     * @param id The id to search for
     * @return The game grids found.
     */
    @Override
    public String getGridsById(Long id) {
        String apiUrl = EXTERNAL_API_URL + "grids/game/" + id;
        return webClient.get()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}