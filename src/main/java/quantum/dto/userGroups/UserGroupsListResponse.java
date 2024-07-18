package quantum.dto.userGroups;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.dto.group.GroupResponse;
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
public class UserGroupsListResponse {

    @Schema(example = "Accepted", description = "Accepted groups by the user.")
    private List<GroupResponse> accepted;

    @Schema(example = "Pending", description = "Accepted groups by the user.")
    private List<GroupResponse> pending;

}
