package stevi.spring.web.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class HandlerMethod {

    private Method method;

    private Parameter[] parameters;

    private List<MethodParameter> methodParameters;

    private Class<?> beanType;

    private Object bean;


}
