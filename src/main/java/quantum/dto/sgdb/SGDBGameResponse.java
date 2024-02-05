package quantum.dto.sgdb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Body object at endpoint search game by id in sgbd.")
@Validated
public class SGDBGameResponse {
    private long id;
    private String name;
    private long release_date;
    private List<String> types;
    private boolean verified;
}
