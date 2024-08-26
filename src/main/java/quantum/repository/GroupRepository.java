package quantum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.Game;
import quantum.model.Group;

import java.util.Optional;

/**
 * JPA repository connection for {@link Group} entity.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
}
