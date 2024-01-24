package quantum.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.http.HttpClient;

/**
 * General exception for the Quantum Library application.
 */
@Getter
public class QuantumLibraryGenericException extends RuntimeException{

    /**
     * The error code.
     */
    private final String errorCode;

    /**
     * The error message.
     */
    private final String errorMessage;

    /**
     * The {@link HttpStatus} status of the error.
     */
    private final HttpStatus httpStatus;

    /**
     * Instantiates a new Quantum library generic exception.
     * @param errorCode    the error code
     * @param errorMessage the error message
     * @param httpStatus   the http status
     */
    public QuantumLibraryGenericException(String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
