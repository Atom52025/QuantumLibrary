package quantum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import quantum.service.SteamGridBDService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SteamGridBDServiceImpl implements SteamGridBDService {


    private final WebClient webClient;
    private static final String EXTERNAL_API_URL = "https://www.steamgriddb.com/api/v2/";
    private static final String AUTH_TOKEN = "1a8c79dc9eff0c11128e1a230c8abdae";

    @Autowired
    public SteamGridBDServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(EXTERNAL_API_URL).build();
    }

    /**
     * Search game in steam grid db by term.
     * @param term The term to search for
     * @return The games found.
     */
    @Override
    public String searchByTerm(String term) {
        String apiUrl = EXTERNAL_API_URL + "search/autocomplete/" + term;
        return webClient.get()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + AUTH_TOKEN)
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

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
}
