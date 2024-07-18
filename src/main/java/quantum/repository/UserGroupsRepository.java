package quantum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quantum.model.UserGroup;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository connection for {@link UserGroup} entity.
 */
public interface UserGroupsRepository extends JpaRepository<UserGroup, Long> {
    /**
     * JPA Named Query find accepted user groups by username.
     *
     * @param username The username to find.
     * @param accepted The accepted status to find.
     * @return The user groups found.
     */
    List<UserGroup> findByUser_UsernameAndAccepted(String username, Boolean accepted);

    /**
     * JPA Named Query find accepted users in group.
     *
     * @param groupId  The group to find.
     * @param accepted The accepted status to find.
     * @return The user groups found.
     */
    List<UserGroup> findByGroup_IdAndAccepted(Long groupId, Boolean accepted);

    /**
     * JPA Named Query find users in group.
     *
     * @param groupId The group id to find.
     * @return The user groups found.
     */
    List<UserGroup> findByGroup_Id(Long groupId);

    /**
     * JPA Named Query find user groups by group and username.
     *
     * @param username The username to find.
     * @param groupId  The group id to find.
     * @return The user groups found.
     */
    Optional<UserGroup> findByUser_UsernameAndGroup_Id(String username, Long groupId);
}
