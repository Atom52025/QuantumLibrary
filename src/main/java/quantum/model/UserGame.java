package quantum.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

/**
 * Entity class for User Games
 */

@Entity
@Getter
@Setter
@ToString
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_GAMES")
public class UserGame {

    @Id
    @Column(name = "USER_GAME_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "GAME_ID")
    private Game game;

    @Column(name = "TIME_PLAYED")
    private Integer timePlayed;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "TAGS")
    private Set<String> tags;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "ACHIVEMENTS")
    private Integer achivements;

    @Column(name = "TOTAL_ACHIVEMENTS")
    private Integer totalAchivements;

    @Column(name = "FINISHED")
    private Boolean finished;

    @Column(name = "FAVORITE")
    private Boolean favorite;

    @Column(name = "BACKLOG")
    private Integer backlog;

}
