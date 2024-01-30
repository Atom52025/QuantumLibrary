package quantum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.UserGame;

import java.util.List;

/**
 * JPA repository connection for {@link UserGame} entity.
 */
public interface UserGamesRepository extends JpaRepository<UserGame, Long> {

    /**
     * JPA Named Query find a user by username.
     * @param userId The username to find.
     * @param pageable The pagination information.
     * @return The user found.
     */
    List<UserGame> findByUserId(Long userId, Pageable pageable);
}
