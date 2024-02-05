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
     * @param entity The element to convert.
     * @return The converted element.
     */
    @Mapping(expression = "java(java.util.Arrays.asList(entity.getTags().split(\", \")))", target = "tags")
    GameResponse map(Game entity);
}
