package stevi.spring.web.servlet.responsebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

public class JsonResponseBuilder implements ResponseBuilder {

    private final ObjectMapper objectMapper;

    public JsonResponseBuilder() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    @SneakyThrows
    public void buildResponse(Object model, int statusCode, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        if (model != null) {
            response.getWriter().write(objectMapper.writeValueAsString(model));
        }
    }
}
