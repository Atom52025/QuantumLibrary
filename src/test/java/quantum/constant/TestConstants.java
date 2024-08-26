package quantum.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import quantum.model.Game;
import quantum.model.User;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {
    public static final String SAMPLE_USERNAME = "UserName";
    public static final String SAMPLE_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final Game SAMPLE_GAME = Game.builder()
            .id(1L)
            .name("GameName")
            .tags(Set.of("GameTag1", "GameTag2"))
            .image("GameImage")
            .sgdbId(1L)
            .build();
    public static final User SAMPLE_USER = User.builder()
            .id(1L)
            .username("UserName")
            .password("UserPassword")
            .email("UserEmail")
            .role("USER")
            .image("UserImage")
            .build();

}
