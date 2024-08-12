package quantum.dto.userGames;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quantum.model.Game;
import quantum.model.User;

import java.util.List;

/**
 * Data transfer object for {@link User} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for Users entity.")
public class StatsResponse {

    @Schema(example = "1", description = "Number of games.")
    @JsonProperty("numOfGames")
    private BacklogResponse numOfGames;

    @Schema(example = "1", description = "Number of completed games.")
    @JsonProperty("numOfCompletedGames")
    private Integer numOfCompletedGames;

    @Schema(example = "1", description = "Number of finished games.")
    @JsonProperty("numOfFinishedGames")
    private Integer numOfFinishedGames;

    @Schema(example = "1", description = "Number of played games.")
    @JsonProperty("numOfPlayedGames")
    private BacklogResponse numOfPlayedGames;

    @Schema(example = "1", description = "Number of total achievements of games.")
    @JsonProperty("numOfTotalAchivements")
    private BacklogResponse numOfTotalAchivements;

    @Schema(example = "1", description = "Number of total time spent on games.")
    @JsonProperty("numOfTotalTime")
    private BacklogResponse numOfTotalTime;

    @Schema(example = "1", description = "Number of completed achievements of games.")
    @JsonProperty("numOfCompletedAchivements")
    private BacklogResponse numOfCompletedAchivements;




}
