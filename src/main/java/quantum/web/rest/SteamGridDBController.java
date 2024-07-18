package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.service.SteamGridDBService;
import quantum.web.api.SteamGridDBApi;

/**
 * Controller for Steam Grid DB Petitions.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class SteamGridDBController implements SteamGridDBApi {

    private final SteamGridDBService service;

    /**
     * GET to /api/sgdb/search to search in sgdb by term.
     *
     * @param term The term to search for
     * @return The games founded.
     */
    @Override
    public ResponseEntity<String> searchByTerm(String term) {
        return ResponseEntity.ok(service.searchByTerm(term));
    }

    /**
     * GET to /api/sgdb/getGrids to search in sgdb by term.
     *
     * @param gameSgdbId The SGDB game id.
     * @return The games founded.
     */
    @Override
    public ResponseEntity<String> getGrids(Long gameSgdbId) {
        return ResponseEntity.ok(service.getGridsById(gameSgdbId));
    }
}
