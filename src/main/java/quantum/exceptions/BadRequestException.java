package quantum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static quantum.constant.ErrorConstants.BAD_REQUEST_ERROR;

/**
 * Exception to throw when the request is bad.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends QuantumLibraryGenericException {

    private static final String DEFAULT_MESSAGE = "Some parameter you sent was missing or had a wrong format.";

    /**
     * Constructor for BadRequestException with a default message.
     */
    public BadRequestException() {
        super(BAD_REQUEST_ERROR, DEFAULT_MESSAGE, HttpStatus.FOUND);
    }

    /**
     * Constructor for BadRequestException with a custom message.
     *
     * @param message The custom error message.
     */
    public BadRequestException(String message) {
        super(BAD_REQUEST_ERROR, message, HttpStatus.FOUND);
    }
}