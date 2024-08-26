package quantum.dto.auth;

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
@Schema(description = "Body object at endpoint sign up.")
@Validated
public class SignUpBody {
    @Schema(example = "User Name", description = "Name of the user.")
    @JsonProperty("username")
    private String username;

    @Schema(example = "User Mail", description = "Mail of the user.")
    @JsonProperty("email")
    private String email;

    @Schema(example = "User Password", description = "Password of the user.")
    @JsonProperty("password")
    private String password;
}

