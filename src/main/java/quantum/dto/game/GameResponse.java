package quantum.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import quantum.model.Game;

import java.util.List;

/**
 * Data transfer object for {@link Game} entity.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Games entity.")
public class GameResponse {

    @Schema(example = "1", description = "Id of the game.")
    @JsonProperty("id")
    private Long id;

    @Schema(example = "Game Name", description = "Name of the game.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "Game Image", description = "Image of the game.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "Game Tags", description = "Tags of the game.")
    @JsonProperty("tags")
    private List<String> tags;

    @Schema(example = "Steam Grid DB Id", description = "The id of the game in the Steam Grid DB.")
    @JsonProperty("sgdbId")
    private Long sgdbId;

}
