package quantum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static quantum.constant.ErrorConstants.ENTITY_FOUND_ERROR;

/**
 * Exception to throw when the entity is found.
 */
@ResponseStatus(HttpStatus.FOUND)
public class EntityFoundException extends QuantumLibraryGenericException {

    private static final String DEFAULT_MESSAGE = "The entity already exists in the given context.";

    /**
     * Constructor for EntityAlreadyExistsException with a default message.
     */
    public EntityFoundException() {
        super(ENTITY_FOUND_ERROR, DEFAULT_MESSAGE, HttpStatus.FOUND);
    }

    /**
     * Constructor for EntityAlreadyExistsException with a custom message.
     *
     * @param message The custom error message.
     */
    public EntityFoundException(String message) {
        super(ENTITY_FOUND_ERROR, message, HttpStatus.FOUND);
    }
}