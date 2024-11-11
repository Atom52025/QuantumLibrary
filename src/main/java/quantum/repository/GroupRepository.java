package quantum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.Group;

/**
 * JPA repository connection for {@link Group} entity.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
}
