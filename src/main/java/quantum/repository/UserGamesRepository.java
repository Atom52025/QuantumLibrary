package quantum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import quantum.model.UserGame;
import quantum.repository.projections.UserGameSgdbIdProjection;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * JPA repository connection for {@link UserGame} entity.
 */
public interface UserGamesRepository extends JpaRepository<UserGame, Long> {

    /**
     * JPA Named Query find a user by username.
     *
     * @param username The username to find.
     * @param pageable The pagination information.
     * @return The user found.
     */
    Page<UserGame> findByUser_Username(String username, Pageable pageable);

    /**
     * JPA Named Query find list of games sgbdId of a user by username.
     *
     * @param username The username to find.
     * @return The user found.
     */
    List<UserGameSgdbIdProjection> findByUser_Username(String username);

    /**
     * JPA Named Query find a user by username.
     *
     * @param username The username to find.
     * @param backlog  The backlog to find.
     * @param pageable The pagination information.
     * @return The user found.
     */
    Page<UserGame> findByUser_UsernameAndBacklog(String username, Integer backlog, Pageable pageable);

    /**
     * JPA Named Query find favourite user games by username.
     *
     * @param username  The username to find.
     * @param favourite The favourite value to find.
     * @param pageable  The pagination information.
     * @return The user found.
     */
    Page<UserGame> findByUser_UsernameAndFavorite(String username, Boolean favourite, Pageable pageable);

    /**
     * JPA Named Query find user games finished by username.
     *
     * @param username The username to find.
     * @param finished The finished value to find.
     * @param pageable The pagination information.
     * @return The user found.
     */
    Page<UserGame> findByUser_UsernameAndFinished(String username, Boolean finished, Pageable pageable);

    /**
     * JPA Named Query find user games completed by username.
     *
     * @param username The username to find.
     * @param tags     The tags to find.
     * @return The user found.
     */
    List<UserGame> findByUser_UsernameAndTagsIn(String username, Set<String> tags);

    /**
     * JPA Named Query find a user by username.
     *
     * @param username The username to find.
     * @param pageable The pagination information.
     * @return The user found.
     */
    @Query("SELECT ug FROM UserGame ug WHERE ug.user.username = :username AND ug.achivements = ug.totalAchivements")
    Page<UserGame> findByUser_UsernameAndCompleted(String username, Pageable pageable);

    /**
     * JPA Named Query find a user game by username and game id.
     *
     * @param username The username to find.
     * @param gameId   The game id to find.
     * @return The user found.
     */
    Optional<UserGame> findByUser_UsernameAndGame_Id(String username, Long gameId);

    /**
     * JPA Named Query find a user game by username and sgdb id.
     *
     * @param username The username to find.
     * @param sgdbId   The game id to find.
     * @return The user found.
     */
    Optional<UserGame> findByUser_UsernameAndGame_SgdbId(String username, Long sgdbId);
}
