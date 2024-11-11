package quantum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import quantum.utils.LongListConverter;

import java.util.List;

/**
 * Entity class for User Groups
 */

@Entity
@Getter
@Setter
@ToString(exclude = {"group"})
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_GROUPS")
public class UserGroup {
    @Id
    @Column(name = "USER_GROUP_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @Column(name = "ACCEPTED")
    private Boolean accepted;

    @Convert(converter = LongListConverter.class)
    @Column(name = "VOTED")
    private List<Long> voted;
}
