package quantum.dto.usergames;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.model.Game;
import quantum.model.User;
import quantum.validation.Alphanumeric;

/**
 * Data transfer object for {@link User} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Users entity.")
public class UserGameResponse {

    @Schema(example = "Game", description = "Game.")
    @JsonProperty("game")
    private Game game;

    @Schema(example = "Time played", description = "Time played on the game.")
    @JsonProperty("timePlayed")
    private int timePlayed;
}
