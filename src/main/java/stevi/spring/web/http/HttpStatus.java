package stevi.spring.web.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration of HTTP status codes.
 */

@Getter
@AllArgsConstructor
public enum HttpStatus {

    OK(200, "OK"),

    CREATED(201, "Created"),

    ACCEPTED(202, "Accepted"),

    NO_CONTENT(204, "No Content"),

    PARTIAL_CONTENT(206, "Partial Content"),

    MULTIPLE_CHOICES(300, "Multiple Choices"),

    MOVED_PERMANENTLY(301, "Moved Permanently"),

    FOUND(302, "Found"),

    SEE_OTHER(303, "See Other"),

    NOT_MODIFIED(304, "Not Modified"),

    BAD_REQUEST(400, "Bad Request"),

    UNAUTHORIZED(401, "Unauthorized"),

    PAYMENT_REQUIRED(402, "Payment Required"),

    FORBIDDEN(403, "Forbidden"),

    NOT_FOUND(404, "Not Found"),

    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    NOT_ACCEPTABLE(406, "Not Acceptable"),

    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),

    REQUEST_TIMEOUT(408, "Request Timeout"),

    CONFLICT(409, "Conflict"),

    GONE(410, "Gone"),

    PRECONDITION_FAILED(412, "Precondition Failed"),

    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),

    URI_TOO_LONG(414, "URI Too Long"),

    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),

    EXPECTATION_FAILED(417, "Expectation Failed"),

    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),

    LOCKED(423, "Locked"),

    FAILED_DEPENDENCY(424, "Failed Dependency"),

    TOO_EARLY(425, "Too Early"),

    UPGRADE_REQUIRED(426, "Upgrade Required"),

    PRECONDITION_REQUIRED(428, "Precondition Required"),

    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    NOT_IMPLEMENTED(501, "Not Implemented"),

    BAD_GATEWAY(502, "Bad Gateway"),

    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),

    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),

    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

    private static final HttpStatus[] VALUES;

    static {
        VALUES = values();
    }

    private final int value;

    private final String reasonPhrase;

    /**
     * Return the {@code HttpStatus} enum constant with the specified numeric value.
     *
     * @param statusCode the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     */
    public static HttpStatus valueOf(int statusCode) {
        for (HttpStatus status : VALUES) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }

}
