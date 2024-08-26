package quantum.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.model.User;

/**
 * Data transfer object for {@link User} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Users entity.")
public class AuthResponse {

    @Schema(example = "1", description = "Id of the user.")
    @JsonProperty("id")
    private Long id;

    @Schema(example = "User Name", description = "Name of the game.")
    @JsonProperty("username")
    private String username;

    @Schema(example = "User Mail", description = "Mail of the user.")
    @JsonProperty("email")
    private String email;

    @Schema(example = "User Image", description = "Image of the user.")
    @JsonProperty("image")
    private String image;

    @Schema(example = "User Role", description = "Role of the user.")
    @JsonProperty("role")
    private String role;

    @Schema(example = "JWT Token", description = "JWT Authentication Token.")
    @JsonProperty("token")
    private String token;
}
