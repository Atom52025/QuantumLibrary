package quantum.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import quantum.dto.game.GameResponse;
import quantum.dto.group.GroupResponse;
import quantum.model.Game;
import quantum.model.Group;

/**
 * Map struct mapper for {@link Group} related objects.
 */
@Mapper(componentModel = "spring")
public interface GroupMapping {

    /**
     * Map {@link Group} to {@link GroupResponse}.
     *
     * @param entity The element to convert.
     * @return The converted element.
     */
    GroupResponse map(Group entity);
}
