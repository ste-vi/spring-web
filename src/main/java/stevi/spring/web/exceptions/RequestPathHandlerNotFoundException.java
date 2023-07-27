package stevi.spring.web.exceptions;

import stevi.spring.web.http.HttpStatus;

import java.time.LocalDateTime;

public class RequestPathHandlerNotFoundException extends FrameworkException {

    private final ExceptionResponse exceptionResponse;

    public RequestPathHandlerNotFoundException(String detailedDescription) {
        exceptionResponse = new ExceptionResponse(LocalDateTime.now().toString(), HttpStatus.NOT_FOUND.getValue(), "Not Found", detailedDescription);
    }

    @Override
    public ExceptionResponse getExceptionResponse() {
        return this.exceptionResponse;
    }
}
