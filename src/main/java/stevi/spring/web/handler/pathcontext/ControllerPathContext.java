package stevi.spring.web.handler.pathcontext;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ControllerPathContext {

    private String requestMappingPath;
    private List<MethodPathContext> methodPathContexts;
}
