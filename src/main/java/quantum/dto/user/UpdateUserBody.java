package quantum.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import quantum.validation.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Body object at endpoint update user.")
@Validated
public class UpdateUserBody {
    @Schema(example = "User Mail", description = "Mail of the user.")
    @Email
    @JsonProperty("email")
    private String email;

    @Schema(example = "User Image", description = "Image of the user.")
    @JsonProperty("image")
    private String image;
}
