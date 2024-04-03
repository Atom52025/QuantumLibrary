package quantum.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import quantum.exceptions.DatabaseConnectionException;
import quantum.exceptions.EntityNotFoundException;

/**
 * Error constants.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

    /**
     * Default error code for {@link DatabaseConnectionException}.
     */
    public static final String DATABASE_ERROR = "Database connection error.";

    /**
     * Default error code for {@link EntityNotFoundException}.
     */
    public static final String ENTITY_NOT_FOUND_ERROR = "Entity not found.";

    /**
     * Default error code for {@link DatabaseConnectionException}.
     */
    public static final String DATA_INTEGRITY_ERROR = "Incorrect parameter format insertion.";
}
