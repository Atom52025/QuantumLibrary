package quantum.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import quantum.model.Game;
import quantum.model.Group;
import quantum.model.User;
import quantum.model.UserGroup;

import java.util.ArrayList;
import java.util.List;
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

    public static Group SAMPLE_GROUP = Group.builder()
            .id(1L)
            .name("GroupName")
            .userGroups(new ArrayList<>())
            .build();

    public static final UserGroup SAMPLE_USER_GROUP = UserGroup.builder()
            .id(1L)
            .user(SAMPLE_USER)
            .group(SAMPLE_GROUP)
            .accepted(true)
            .voted(new ArrayList<>())
            .build();


}
