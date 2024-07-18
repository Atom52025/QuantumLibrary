package quantum.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quantum.dto.group.GroupGamesResponse;
import quantum.model.UserGroup;

/**
 * The api interface for {@link UserGroup} entity.
 */

@Validated
public interface GroupGamesApi {

    /**
     * GET to /api/group/{group_id} to fetch a group games list.
     *
     * @param token    The token with the authentication information.
     * @param group_id The group id.
     * @return The list of groups the user is in or is invited.
     */
    @Operation(summary = "Get group games", description = "Get group games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of games"),
            @ApiResponse(responseCode = "400", description = "Invalid page or page size value"),
            @ApiResponse(responseCode = "404", description = "No results found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/api/group/{group_id}/games", produces = "application/json")
    ResponseEntity<GroupGamesResponse> getGroupGames(
            @RequestHeader("Authorization")
            String token,
            @Parameter(in = ParameterIn.PATH, required = true, description = "The group id")
            @PathVariable("group_id")
            Long group_id
    );

}
