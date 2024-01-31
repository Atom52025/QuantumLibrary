package quantum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.UserGame;

/**
 * JPA repository connection for {@link UserGame} entity.
 */
public interface UserGamesRepository extends JpaRepository<UserGame, Long> {

    /**
     * JPA Named Query find a user by username.
     * @param username The username to find.
     * @param pageable The pagination information.
     * @return The user found.
     */
    Page<UserGame> findByUser_Username(String username, Pageable pageable);

    /**
     * JPA Named Query find a user game by username and game id.
     * @param username The username to find.
     * @param gameId The game id to find.
     * @return The user found.
     */
    UserGame findByUser_UsernameAndGame_Id(String username, Long gameId);
}
