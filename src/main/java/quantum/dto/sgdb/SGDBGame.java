package quantum.dto.sgdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Schema(description = "Body object at endpoint search game by id in sgdb.")
@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class SGDBGame {
    private Long id;
    private String name;
    private Long release_date;
    private List<String> types;
    private Boolean verified;
}
