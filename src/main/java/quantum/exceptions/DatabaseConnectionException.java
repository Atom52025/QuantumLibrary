package quantum.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static quantum.constant.ErrorConstants.DATABASE_ERROR;


/**
 * Exception to throw when there are database connection issues.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseConnectionException extends QuantumLibraryGenericException {

    /**
     * Constructor for DatabaseConnectionException with a message.
     * @param ex the error to pass.
     */
    public DatabaseConnectionException(Throwable ex) {
        super(DATABASE_ERROR, ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructor for DatabaseConnectionException with a code and a message.
     * @param errorCode the error code to pass.
     * @param ex the error pass.
     */
    public DatabaseConnectionException(String errorCode, Throwable ex) {
        super(errorCode, ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
