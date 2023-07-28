package stevi.spring.web.exceptions;

/**
 * Record object which is returned as an exception response.
 */
public record ExceptionResponse(String timestamp, int statusCode, String reason, String detailedDescription) {

}
