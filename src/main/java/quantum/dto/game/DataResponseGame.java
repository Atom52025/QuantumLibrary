package quantum.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.model.Game;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Data transfer object for {@link Game} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Games entity.")
public class DataResponseGame {

    @Schema(example = "1", description = "Id of the game.")
    @JsonProperty("id")
    private Long id;

    @Schema(example = "Game Name", description = "Name of the game.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "Game Description", description = "Description of the game.")
    @JsonProperty("description")
    private String description;

    @Schema(example = "Game Image", description = "Image of the game.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "Game Tags", description = "Tags of the game.")
    @JsonProperty("tags")
    private List<String> tags;

}
