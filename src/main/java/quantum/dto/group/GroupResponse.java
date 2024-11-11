package quantum.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.dto.userGroups.UserGroupResponse;
import quantum.model.Group;

import java.util.List;

/**
 * Data transfer object for {@link Group} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Groups entity.")
public class GroupResponse {

    @Schema(example = "1", description = "Id of the group.")
    @JsonProperty("id")
    private Long id;

    @Schema(example = "Group Name", description = "Name of the group.")
    @JsonProperty("name")
    private String name;

    @Schema(example = "1", description = "Number of users in the group.")
    @JsonProperty("users")
    private List<UserGroupResponse> userGroups;

}
