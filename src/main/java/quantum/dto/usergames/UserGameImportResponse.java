package quantum.dto.userGames.steamImport;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.model.Game;
import quantum.model.User;

import java.util.List;

/**
 * Data transfer object for user game imports.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for user game imports.")
public class UserGameImportResponse {

    @Schema(example = "Portal2", description = "Game name.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "423423", description = "Time played on minutes.")
    @JsonProperty("timePlayed")
    private Integer timePlayed;

    @Schema(example = "https://cdn2.steamgriddb.com/thumb/0aabd89a5b780073e64ef9d05fa69311.jpg", description = "The grid image for the game.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "32131", description = "The SGBD id of the game.")
    @JsonProperty("sgbdId")
    private Long sgbdId;

}
