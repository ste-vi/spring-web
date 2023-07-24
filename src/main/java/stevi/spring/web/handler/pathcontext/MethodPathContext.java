package stevi.spring.web.handler.pathcontext;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Builder
@Data
public class MethodPathContext {

    private String methodFullPath;
    private Method method;
    private List<MethodParameterPathContext> methodParameterPathContexts;

}
