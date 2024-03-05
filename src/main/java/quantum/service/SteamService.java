package quantum.service;

import quantum.dto.usergames.UserGamesImportListResponse;

/**
 * Service for Steam Grid DB Petitions.
 */
public interface SteamService {

    /**
     * Get steam user.
     * @param steamId The steam id to search for
     * @return The user found.
     */
    String getUser(String steamId);

    /**
     * Get steam games.
     * @param steamId The steam id to search for
     * @return The games found.
     */
    UserGamesImportListResponse getGames(String steamId);


}
