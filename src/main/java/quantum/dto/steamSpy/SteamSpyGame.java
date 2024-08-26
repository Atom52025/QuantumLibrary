package quantum.dto.steamSpy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Body object  at endpoint games in spy steam.")
@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamSpyGame {
    @JsonProperty("appid")
    private Long appId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("tags")
    private Map<String, Integer> tags;
}
