package stevi.spring.web.handler.pathcontext;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MethodParameterPathContext {

    private String javaParameterName;
    private String urlParameterName;
}
