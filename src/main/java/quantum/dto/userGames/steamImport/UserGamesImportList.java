package quantum.dto.userGames.steamImport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.dto.userGames.UserGameResponse;

import java.util.List;

/**
 * {@link UserGameResponse} list.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for User Games entity.")
public class UserGamesImportList {

    @Schema(description = "List of games of a user.")
    private List<UserGameImport> games;
}
