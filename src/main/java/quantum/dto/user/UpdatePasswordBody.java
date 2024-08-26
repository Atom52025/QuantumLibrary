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
@Schema(description = "Body object at endpoint update password.")
@Validated
public class UpdatePasswordBody {
    @Schema(example = "4321423", description = "The old password.")
    @JsonProperty("oldPassword")
    private String oldPassword;

    @Schema(example = "2342342", description = "The new password.")
    @JsonProperty("newPassword")
    private String newPassword;

}
