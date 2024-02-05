package quantum.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.dto.common.DataResponseTags;

import java.util.List;

/**
 * {@link DataResponseTags} list.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Games entity.")
public class GameListResponse {

    @Schema(description = "List of games.")
    private List<GameResponse> games;
}
