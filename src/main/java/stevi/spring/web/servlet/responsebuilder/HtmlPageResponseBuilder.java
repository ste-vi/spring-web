package stevi.spring.web.servlet.responsebuilder;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

/**
 * Class which builds HTML page response
 */
public class HtmlPageResponseBuilder implements ResponseBuilder {

    /**
     * Takes model object and status codes and writes it into response.
     */
    @Override
    @SneakyThrows
    public void buildResponse(Object model, int statusCode, HttpServletResponse response) {
        throw new UnsupportedOperationException("HTML pages not yet supported, please return object to which will be mapped into JSON format");
    }
}
