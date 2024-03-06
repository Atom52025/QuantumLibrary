package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import quantum.dto.userGames.steamImport.UserGamesImportList;

/**
 * The api interface for steam request.
 */

@Validated
@RequestMapping("/api/steam")
public interface SteamApi {

    /**
     * GET to /api/steam/user/{steam_id} to search a user by its userId in steam.
     * @param steamId The steam id to search for
     * @return The user found.
     */
    @Operation(summary = "Search a user by its userId in steam", description = "Search a user by its userId in steam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user founded"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/user/{steam_id}", produces = "application/json")
    ResponseEntity<String> getUser(
            @PathVariable("steam_id")
                String steamId);

    /**
     * GET to /api/steam/games/{steam_id} to search user games.
     * @param steamId The steam id to search for
     * @return The games founded.
     */
    @Operation(summary = "Search in SGDB for a name by its name", description = "Search in SGDB for a name by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The games founded"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/games/{steam_id}", produces = "application/json")
    ResponseEntity<UserGamesImportList> getGames(
            @PathVariable("steam_id")
                String steamId);

};



