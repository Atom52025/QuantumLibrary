package quantum.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import quantum.dto.userGames.UserGameResponse;
import quantum.dto.userGroups.UserGroupResponse;
import quantum.model.UserGame;
import quantum.model.UserGroup;

/**
 * Map struct mapper for {@link UserGroup} related objects.
 */
@Mapper(componentModel = "spring")
public interface UserGroupsMapping {

    /**
     * Map {@link UserGroup} to {@link UserGroupResponse}.
     *
     * @param entity The element to convert.
     * @return The converted element.
     */
    UserGroupResponse map(UserGroup entity);

}
