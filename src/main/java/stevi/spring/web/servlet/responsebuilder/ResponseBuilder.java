package stevi.spring.web.servlet.responsebuilder;

import jakarta.servlet.http.HttpServletResponse;

public interface ResponseBuilder {

    void buildResponse(Object model, int statusCode, HttpServletResponse response);
}
