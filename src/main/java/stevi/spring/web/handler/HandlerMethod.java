package stevi.spring.web.handler;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Builder
@Data
public class HandlerMethod {

    private Method method;

    private final Parameter[] parameters;

    private final Class<?> beanType;

    private final Object bean;

}
