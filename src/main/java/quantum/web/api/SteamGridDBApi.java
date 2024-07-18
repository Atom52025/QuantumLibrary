package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The api interface for sgdb request.
 */

@Validated
@RequestMapping("/api/sgdb")
public interface SteamGridDBApi {

    /**
     * GET to /api/sgdb/search to search in sgdb by term.
     *
     * @param term The term to search for
     * @return The games founded.
     */
    @Operation(summary = "Search in SGDB for a name by its name", description = "Search in SGDB for a name by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The games founded"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/search", produces = "application/json")
    ResponseEntity<String> searchByTerm(
            @RequestParam(value = "term", required = false)
            String term);

    /**
     * GET to /api/sgdb/getGrids to search in sgdb by term.
     *
     * @param gameSgdbId The SGDB game id.
     * @return The games founded.
     */
    @Operation(summary = "Search in SGDB for a name by its name", description = "Search in SGDB for a name by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The games founded"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/getGrids/{game_sgdb_id}", produces = "application/json")
    ResponseEntity<String> getGrids(
            @PathVariable("game_sgdb_id")
            Long gameSgdbId);

}



