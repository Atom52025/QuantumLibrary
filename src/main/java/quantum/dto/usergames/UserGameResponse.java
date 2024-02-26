package quantum.dto.usergames;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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

    @Schema(example = "423423", description = "Time played on minutes.")
    @JsonProperty("timePlayed")
    private Integer timePlayed;

    @Schema(example = "https://cdn2.steamgriddb.com/thumb/0aabd89a5b780073e64ef9d05fa69311.jpg", description = "The grid image for the game.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "mmo, shooter", description = "Tags for the game.")
    @JsonProperty("tags")
    private String tags;

    @Schema(example = "Achivements", description = "Achivements.")
    @JsonProperty("achivements")
    private Integer achivements;

    @Schema(example = "Total achivements", description = "Total achivements.")
    @JsonProperty("totalAchivements")
    private Integer totalAchivements;

    @Schema(example = "Finished", description = "Finished.")
    @JsonProperty("finished")
    private Boolean finished;

    @Schema(example = "Favorite", description = "Favorite.")
    @JsonProperty("favorite")
    private Boolean favorite;

    @Schema(example = "Category", description = "Category.")
    @JsonProperty("category")
    private String category;
}
