package quantum.dto.sgdb;

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
@Schema(description = "Body object at endpoint search game by id in sgbd.")
@Validated
public class SGBDGameSuccessResponse {
    private boolean success;
    private SGDBGameResponse data;
}
