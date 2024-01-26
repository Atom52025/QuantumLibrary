package quantum.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * Entity class for Users
 */

@Entity
@Getter
@Setter
@ToString
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD", length = 60)
    private String password;
}
