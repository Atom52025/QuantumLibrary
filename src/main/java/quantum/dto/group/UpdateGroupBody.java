package quantum.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Body object at endpoint update group.")
@Validated
public class UpdateGroupBody {
    @Schema(example = "Group name", description = "Name of the group.")
    @JsonProperty("name")
    private String name;
}
