package quantum.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import quantum.dto.game.GameResponse;
import quantum.dto.sgdb.SGDBGame;
import quantum.dto.sgdb.SGDBGrid;
import quantum.dto.steamSpy.SteamSpyGame;
import quantum.dto.userGames.steamImport.UserGameImport;
import quantum.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {
    public static final String SAMPLE_USERNAME = "UserName";
    public static final String SAMPLE_TOKEN = "sampleToken";

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

    public static final UserGame SAMPLE_USER_GAME = UserGame.builder()
            .id(1L)
            .user(SAMPLE_USER)
            .game(SAMPLE_GAME)
            .timePlayed(123)
            .image("GameImage")
            .tags(Set.of("GameTag1", "GameTag2"))
            .achivements(1)
            .totalAchivements(2)
            .finished(true)
            .favorite(true)
            .backlog(1)
            .build();
}
