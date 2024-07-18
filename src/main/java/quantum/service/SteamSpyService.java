package quantum.service;

import quantum.dto.steamSpy.SteamSpyGame;

/**
 * Service for Steam Grid DB Petitions.
 */
public interface SteamSpyService {

    /**
     * Get steam spy info.
     *
     * @param steamId The steam id to search for
     * @return The info found.
     */
    SteamSpyGame getSteamSpyInfo(Long steamId) ;
}
