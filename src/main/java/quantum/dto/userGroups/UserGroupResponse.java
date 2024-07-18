package quantum.dto.userGroups;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.dto.group.GroupResponse;
import quantum.dto.user.UserResponse;
import quantum.model.Group;
import quantum.model.User;

import java.util.List;

/**
 * Data transfer object for {@link Group} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Groups entity.")
public class UserGroupResponse {

    @Schema(example = "User", description = "User.")
    @JsonProperty("user")
    private UserResponse user;

    @Schema(example = "true", description = "Accepted group boolean.")
    @JsonProperty("accepted")
    private Boolean accepted;

    @Schema(example = "1", description = "Voted games.")
    @JsonProperty("voted")
    private String voted;
}
