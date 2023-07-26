package stevi.spring.web.handler.pathcontext;

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
