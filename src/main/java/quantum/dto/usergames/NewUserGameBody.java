package quantum.dto.usergames;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import quantum.validation.Alphanumeric;

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

    @Schema(example = "Game name", description = "Game name.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "Tags", description = "Tags.")
    @JsonProperty("tags")
    private String tags;
}
