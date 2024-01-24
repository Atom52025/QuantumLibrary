package quantum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.Game;

/**
 * JPA repository connection for {@link Game} entity.
 */
public interface GameRepository extends JpaRepository<Game, Long> {
}
