package stevi.spring.web.exceptions;

import lombok.Getter;
import stevi.spring.web.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class RequestPathHandlerNotFoundException extends RuntimeException {

    private final ExceptionResponse exceptionResponse;

    public RequestPathHandlerNotFoundException(String detailedDescription) {
        exceptionResponse = new ExceptionResponse(LocalDateTime.now().toString(), HttpStatus.NOT_FOUND.getValue(), "Not Found", detailedDescription);
    }
}
