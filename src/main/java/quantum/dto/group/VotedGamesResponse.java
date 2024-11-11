package quantum.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import quantum.dto.game.GameResponse;
import quantum.model.Group;

import java.util.List;

/**
 * Data transfer object for {@link Group} entity.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Groups entity.")
public class VotedGamesResponse extends GameResponse {
    @Schema(example = "[Laura, Dani]", description = "Tags of the game.")
    @JsonProperty("votes")
    private List<String> votes;
}
