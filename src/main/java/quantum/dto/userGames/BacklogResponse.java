package quantum.dto.userGames;

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
public class BacklogResponse {

    @Schema(example = "[Game]", description = "Result on backlog NA.")
    @JsonProperty("backlogNA")
    private Integer backlogNA;

    @Schema(example = "[Game]", description = "Result on backlog 1.")
    @JsonProperty("backlog1")
    private Integer backlog1;

    @Schema(example = "[Game]", description = "Result on backlog 2.")
    @JsonProperty("backlog2")
    private Integer backlog2;

    @Schema(example = "[Game]", description = "Result on backlog 3.")
    @JsonProperty("backlog3")
    private Integer backlog3;



}
