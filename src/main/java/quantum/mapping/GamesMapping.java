package quantum.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import quantum.dto.game.GameResponse;
import quantum.dto.group.VotedGamesResponse;
import quantum.model.Game;

import java.util.List;

/**
 * Map struct mapper for {@link Game} related objects.
 */
@Mapper(componentModel = "spring")
public interface GamesMapping {

    /**
     * Map {@link Game} to {@link GameResponse}.
     *
     * @param entity The element to convert.
     * @return The converted element.
     */
    GameResponse map(Game entity);

    /**
     * Map {@link Game} to {@link VotedGamesResponse}.
     *
     * @param entity The element to convert.
     * @return The converted element.
     */
    @Mapping(target = "votes", source = "votes")
    VotedGamesResponse map(Game entity, List<String> votes);

    /**
     * Map {@link GameResponse} to {@link Game}.
     *
     * @param entity The element to convert.
     * @return The converted element.
     */
    Game map(GameResponse entity);
}
