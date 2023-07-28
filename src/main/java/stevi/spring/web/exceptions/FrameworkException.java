package stevi.spring.web.exceptions;

/**
 * General exception class which is caught and handled to be returned with meaningfully JSON response.<br>
 * Can be extended with more concrete exception class which automatically will be handled.
 */
public abstract class FrameworkException extends RuntimeException {

    /**
     * Gets exception response object to convert into JSON.
     *
     * @return {@link ExceptionResponse}
     */
    public abstract ExceptionResponse getExceptionResponse();
}
