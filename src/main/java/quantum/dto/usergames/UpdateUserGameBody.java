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
    @Schema(example = "Time played", description = "Time played on the game.")
    @JsonProperty("timePlayed")
    private int timePlayed;
}
