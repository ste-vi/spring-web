package stevi.spring.web.exceptions;

public abstract class FrameworkException extends RuntimeException {

    public abstract ExceptionResponse getExceptionResponse();
}
