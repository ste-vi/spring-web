package stevi.spring.web.servlet.mappingresolver.pathcontext;

import lombok.Builder;
import lombok.Data;
import stevi.spring.web.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;

/**
 * POJO which contains all needed information for resolving method from url.
 */
@Builder
@Data
public class MethodPathContext {

    private Method method;
    private String methodFullPath;
    private HttpMethod httpMethod;
    private List<MethodParameterPathContext> methodParameterPathContexts;

}
