package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.service.SteamGridBDService;
import quantum.web.api.SteamGridDBApi;

/**
 * Controller for Steam Grid DB Petitions.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class SteamGridDBController implements SteamGridDBApi {

    private final SteamGridBDService steamGridBDService;

    /**
     * GET to /api/sgbd/search to search in sgbd by term.
     * @param term The term to search for
     * @return The games founded.
     */
    @Override
    public ResponseEntity<String> searchByTerm(String term) {
        return ResponseEntity.ok(steamGridBDService.searchByTerm(term));
    }
}
