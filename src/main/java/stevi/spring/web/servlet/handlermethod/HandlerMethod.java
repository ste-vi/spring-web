package stevi.spring.web.servlet.handlermethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class HandlerMethod {

    private Method method;

    private List<MethodParameter> methodParameters;

    private Object bean;
}
