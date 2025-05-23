package quantum.dto.game;

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
@Schema(description = "Body object at endpoint update game.")
@Validated
public class UpdateGameBody {
    @Schema(example = "Game Name", description = "Name of the game.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "Game Image", description = "Image of the game.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "Game Tags", description = "Tags of the game.")
    @JsonProperty("tags")
    private Set<String> tags;
}
