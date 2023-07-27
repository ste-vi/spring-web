package stevi.spring.web.servlet.responsebuilder;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

public class HtmlPageResponseBuilder implements ResponseBuilder {

    @Override
    @SneakyThrows
    public void buildResponse(Object model, int statusCode, HttpServletResponse response) {
        throw new UnsupportedOperationException("HTML pages not yet supported, please return object to which will be mapped into JSON format");
    }
}
