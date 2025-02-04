package quantum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static quantum.constant.ErrorConstants.BAD_PASSWORD_ERROR;

/**
 * Exception to throw when the request is bad.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadPasswordException extends QuantumLibraryGenericException {

    private static final String DEFAULT_MESSAGE = "The current password you provided is wrong, please, try again.";

    /**
     * Constructor for BadRequestException with a default message.
     */
    public BadPasswordException() {
        super(BAD_PASSWORD_ERROR, DEFAULT_MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Constructor for BadRequestException with a custom message.
     *
     * @param message The custom error message.
     */
    public BadPasswordException(String message) {
        super(BAD_PASSWORD_ERROR, message, HttpStatus.UNAUTHORIZED);
    }
}