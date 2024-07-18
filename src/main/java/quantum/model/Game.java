package quantum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
@Table(name = "GAMES_T")
public class Game {

    @Id
    @Column(name = "GAME_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IMAGE")
    private String image;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "TAGS")
    private Set<String> tags;

    @Column(name = "SGDB_ID")
    private Long sgdbId;

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<UserGame> userGames;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return id.equals(game.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
