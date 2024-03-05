package quantum.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import quantum.dto.usergames.UserGamesImportListResponse;
import quantum.service.SteamGridBDService;
import quantum.service.SteamService;
import quantum.web.api.SteamApi;
import quantum.web.api.SteamGridDBApi;

/**
 * Controller for Steam Grid DB Petitions.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class SteamController implements SteamApi {

    private final SteamService steamService;

    /**
     * GET to /api/steam/user/{steam_id} to search a user by its userId in steam.
     * @param steamId The steam id to search for
     * @return The user found.
     */
    @Override
    public ResponseEntity<String> getUser(String steamId) {
        return ResponseEntity.ok(steamService.getUser(steamId));
    }

    /**
     * GET to /api/steam/games/{steam_id} to search user games.
     * @param steamId The steam id to search for
     * @return The games founded.
     */
    @Override
    public ResponseEntity<UserGamesImportListResponse> getGames(String steamId) {
        return ResponseEntity.ok(steamService.getGames(steamId));
    }
}
