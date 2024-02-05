package quantum.service;

/**
 * Service for Steam Grid DB Petitions.
 */
public interface SteamGridBDService {

    /**
     * Search game in steam grid db by term.
     * @param term The term to search for
     * @return The games found.
     */
    String searchByTerm(String term);

    /**
     * Get game in steam grid db by id.
     * @param id The id to search for
     * @return The game found.
     */
    String getById(Long id);

}
