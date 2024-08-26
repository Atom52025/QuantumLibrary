package quantum.dto.userGames;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * {@link UserGameResponse} list.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for User Games entity.")
public class UserGamesListResponse {

    @Schema(description = "List of games of a user.")
    private List<UserGameResponse> games;
}
