package quantum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static quantum.constant.ErrorConstants.ENTITY_NOT_FOUND_ERROR;

/**
 * Exception to throw when there are database connection issues.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends QuantumLibraryGenericException{
    
    private static final String DEFAULT_MESSAGE = "No results found for the given query.";
    
    /**
     * Constructor for EntityNotFoundException with a message.
     */
    public EntityNotFoundException() {
        super(ENTITY_NOT_FOUND_ERROR, DEFAULT_MESSAGE, HttpStatus.NOT_FOUND);
    }

    /**
     * Constructor for EntityNotFoundException with a code and a message.
     * @param message The error message to pass.
     */
    public EntityNotFoundException(String message) {
        super(ENTITY_NOT_FOUND_ERROR, message, HttpStatus.NOT_FOUND);
    }
}
