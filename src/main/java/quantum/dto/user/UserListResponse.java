package quantum.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.dto.common.DataResponseTags;

import java.util.List;

/**
 * {@link DataResponseTags} list.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Users entity.")
public class UserListResponse {

    @Schema(description = "List of users.")
    private List<UserResponse> users;
}
