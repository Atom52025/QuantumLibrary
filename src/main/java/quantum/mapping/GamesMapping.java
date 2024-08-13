package quantum.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import quantum.dto.game.GameResponse;
import quantum.model.Game;

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
     * Map {@link GameResponse} to {@link Game}.
     *
     * @param entity The element to convert.
     * @return The converted element.
     */
    Game map(GameResponse entity);

}
