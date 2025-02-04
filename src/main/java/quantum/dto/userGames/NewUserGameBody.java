package quantum.dto.userGames;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Body object at endpoint new game.")
@Validated
public class NewUserGameBody {

    @Schema(example = "Time played", description = "Time played on the game.")
    @JsonProperty("timePlayed")
    private Integer timePlayed;

    @Schema(example = "Game image", description = "Game image.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "Game default image", description = "Game default image.")
    @JsonProperty("defaultImage")
    private String defaultImage;

    @Schema(example = "Game name", description = "Game name.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "Tags", description = "Tags.")
    @JsonProperty("tags")
    private Set<String> tags;

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

    @Schema(example = "Backlog", description = "Backlog.")
    @JsonProperty("backlog")
    private Integer backlog;

}
