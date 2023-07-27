package stevi.spring.web.servlet.mappingresolver.pathcontext;

import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;

@Builder
@Data
public class MethodParameterPathContext {

    private String javaParameterName;
    private Class<?> javaParameterType;
    private String urlParameterName;
}
