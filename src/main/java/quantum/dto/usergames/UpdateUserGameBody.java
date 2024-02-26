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
@Schema(description = "Body object at endpoint update user.")
@Validated
public class UpdateUserGameBody {
    @Schema(example = "423423", description = "Time played on minutes.")
    @JsonProperty("timePlayed")
    private Integer timePlayed;

    @Schema(example = "https://cdn2.steamgriddb.com/thumb/0aabd89a5b780073e64ef9d05fa69311.jpg", description = "The grid image for the game.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "mmo, shooter", description = "Tags for the game.")
    @JsonProperty("tags")
    private String tags;
}
