package quantum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.User;

/**
 * JPA repository connection for {@link User} entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
