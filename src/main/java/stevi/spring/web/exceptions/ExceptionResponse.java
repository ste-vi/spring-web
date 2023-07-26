package stevi.spring.web.exceptions;

public record ExceptionResponse(String timestamp, int statusCode, String reason, String detailedDescription) {

}
