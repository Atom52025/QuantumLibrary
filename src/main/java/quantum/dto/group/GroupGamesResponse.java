package quantum.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.dto.game.GameResponse;
import quantum.dto.user.UserResponse;
import quantum.model.Group;

import java.util.List;

/**
 * Data transfer object for {@link Group} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Groups entity.")
public class GroupGamesResponse {

    @Schema(description = "Group.")
    private Group group;

    @Schema(description = "List of games.")
    private List<GameResponse> games;

}
