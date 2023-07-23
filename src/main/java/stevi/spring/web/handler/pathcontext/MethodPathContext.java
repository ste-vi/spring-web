package stevi.spring.web.handler.pathcontext;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MethodPathContext {

    private String methodPath;
    private List<MethodParameterPathContext> methodParameterPathContexts;

}
