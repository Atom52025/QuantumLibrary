package quantum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.User;

import java.util.Optional;

/**
 * JPA repository connection for {@link User} entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * JPA Named Query find a user by username.
     * @param username The username to find.
     * @return The user found.
     */
    Optional<User> findByUsername(String username);
}
