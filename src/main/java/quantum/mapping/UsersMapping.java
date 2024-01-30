package quantum.mapping;

import org.mapstruct.Mapper;
import quantum.dto.auth.AuthResponse;
import quantum.dto.user.UserResponse;
import quantum.model.User;

/**
 * Map struct mapper for {@link User} related objects.
 */
@Mapper(componentModel = "spring")
public interface UsersMapping {

    /**
     * Map {@link User} to {@link UserResponse}.
     * @param entity The element to convert.
     * @return The converted element.
     */
    UserResponse map(User entity);

    /**
     * Map {@link User} to {@link AuthResponse}.
     * @param entity The element to convert.
     * @return The converted element.
     */
    AuthResponse authMap(User entity);
}
