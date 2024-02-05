package quantum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.Game;

import java.util.Optional;

/**
 * JPA repository connection for {@link Game} entity.
 */
public interface GameRepository extends JpaRepository<Game, Long> {
    /**
     * JPA Named Query find a game by sgbd id.
     * @param sgbdId The sgbd game id to find.
     * @return The game found.
     */
    Optional<Game> findBySgbdId(Long sgbdId);
}
