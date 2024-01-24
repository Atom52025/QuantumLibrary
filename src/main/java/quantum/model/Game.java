package quantum.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * Entity class for Games
 */

@Entity
@Getter
@Setter
@ToString
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GAMES")
public class Game {

    @Id
    @Column(name = "GAME_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "TAGS")
    private String tags;

}
