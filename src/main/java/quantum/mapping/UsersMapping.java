package quantum.mapping;

import org.mapstruct.Mapper;
import quantum.dto.user.DataResponseUser;
import quantum.model.User;

/**
 * Map struct mapper for {@link User} related objects.
 */
@Mapper(componentModel = "spring")
public interface UsersMapping {

    /**
     * Map {@link User} to {@link DataResponseUser}.
     * @param entity The element to convert.
     * @return The converted element.
     */
    DataResponseUser map(User entity);
}
