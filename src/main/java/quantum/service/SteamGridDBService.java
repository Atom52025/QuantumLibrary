package quantum.service;

/**
 * Service for Steam Grid DB Petitions.
 */
public interface SteamGridDBService {

    /**
     * Search game in steam grid db by term.
     *
     * @param term The term to search for
     * @return The games found.
     */
    String searchByTerm(String term);

    /**
     * Get game in steam grid db by id.
     *
     * @param id The id to search for
     * @return The game found.
     */
    String getById(Long id);

    /**
     * Get game in steam grid db by steam id.
     *
     * @param id The steam id to search for
     * @return The game found.
     */
    String getBySteamId(Long id);

    /**
     * Get game grids in steam grid db by id.
     *
     * @param id The id to search for
     * @return The game grids found.
     */
    String getGridsById(Long id);

}
