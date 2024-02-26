package quantum.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

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
    @Column(name = "ID")
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

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "IMAGE")
    private String image;

}
