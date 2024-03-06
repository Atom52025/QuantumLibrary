package quantum.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import quantum.dto.userGames.UserGameResponse;
import quantum.model.Game;
import quantum.model.UserGame;

/**
 * Map struct mapper for {@link Game} related objects.
 */
@Mapper(componentModel = "spring")
public interface UserGamesMapping {

    /**
     * Map {@link UserGame} to {@link UserGameResponse}.
     * @param entity The element to convert.
     * @return The converted element.
     */
    @Mapping(source = "entity.game", target = "game")
    @Mapping(expression = "java(java.util.Arrays.asList(entity.getTags().split(\",\")))", target = "tags")
    UserGameResponse map(UserGame entity);

}
