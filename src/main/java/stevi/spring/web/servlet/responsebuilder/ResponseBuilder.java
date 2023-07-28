package stevi.spring.web.servlet.responsebuilder;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Interface which is responsible for building http response.
 */
public interface ResponseBuilder {

    /**
     * Takes model object and status codes and writes it into response.
     */
    void buildResponse(Object model, int statusCode, HttpServletResponse response);
}
