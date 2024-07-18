package quantum.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import quantum.validation.Alphanumeric;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Body object at endpoint new group.")
@Validated
public class NewGroupBody {
    @Schema(example = "Group name", description = "Name of the group.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "[Adri, Laura, Dani]", description = "Usernames of the users invited to the group.")
    @JsonProperty("invited_users")
    private List<String> invited_users;
}
