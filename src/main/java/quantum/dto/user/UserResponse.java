package quantum.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.model.User;
import quantum.validation.Alphanumeric;

/**
 * Data transfer object for {@link User} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Users entity.")
public class UserResponse {
    @Schema(example = "User Name", description = "Name of the game.")
    @JsonProperty("username")
    private String username;

    @Schema(example = "User Mail", description = "Mail of the user.")
    @Alphanumeric(allowedChars = "._-@")
    @JsonProperty("email")
    private String email;

    @Schema(example = "User Role", description = "Role of the user.")
    @Alphanumeric()
    @JsonProperty("role")
    private String role;

    @Schema(example = "User Image", description = "Image of the user.")
    @JsonProperty("image")
    private String image;
}
