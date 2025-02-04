package quantum.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.dto.steamSpy.SteamSpyGame;
import quantum.service.SteamSpyService;

/**
 * Service implementation for Steam API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SteamSpyServiceImpl implements SteamSpyService {

    private static final String EXTERNAL_API_URL = "https://steamspy.com/api.php";
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public SteamSpyServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(EXTERNAL_API_URL).build();
    }

    //------------------------------------- PUBLIC METHODS -------------------------------------//

    /**
     * Get steam spy info.
     *
     * @param steamId The steam id to search for
     * @return The info found.
     */
    public SteamSpyGame getSteamSpyInfo(Long steamId) {
        String spySteamApiUrl = "?request=appdetails&appid=" + steamId;
        String gameResponse = webClient.get()
                .uri(spySteamApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            return objectMapper.readValue(gameResponse, SteamSpyGame.class);
        } catch (Exception e) {
            return null;
        }

    }


}