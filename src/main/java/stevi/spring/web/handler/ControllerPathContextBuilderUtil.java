package stevi.spring.web.handler;

import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.RequestParam;
import stevi.spring.web.handler.pathcontext.ControllerPathContext;
import stevi.spring.web.handler.pathcontext.MethodParameterPathContext;
import stevi.spring.web.handler.pathcontext.MethodPathContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public final class ControllerPathContextBuilderUtil {

    public static ControllerPathContext getRequestPathFromControllerClass(Object controller) {
        Class<?> controllerClass = controller.getClass();
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);

        return ControllerPathContext.builder()
                .requestMappingPath(requestMapping.value())
                .methodPathContexts(getMethodPathContexts(controllerClass))
                .build();
    }

    private static List<MethodPathContext> getMethodPathContexts(Class<?> controllerClass) {
        List<MethodPathContext> methodPathContexts = new ArrayList<>();

        for (var method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);

                MethodPathContext methodPathContext = MethodPathContext.builder()
                        .methodPath(getMapping.value())
                        .methodParameterPathContexts(getMethodParameterPathContexts(method))
                        .build();

                methodPathContexts.add(methodPathContext);
            }
        }
        return methodPathContexts;
    }

    private static List<MethodParameterPathContext> getMethodParameterPathContexts(Method method) {
        List<MethodParameterPathContext> methodParameterPathContexts = new ArrayList<>();
        for (var parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                MethodParameterPathContext parameterPathContext = getParameterPathContext(pathVariable.value(), parameter);
                methodParameterPathContexts.add(parameterPathContext);
            }
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                MethodParameterPathContext parameterPathContext = getParameterPathContext(requestParam.value(), parameter);
                methodParameterPathContexts.add(parameterPathContext);
            }
        }
        return methodParameterPathContexts;
    }

    private static MethodParameterPathContext getParameterPathContext(String pathVariable, Parameter parameter) {
        return MethodParameterPathContext.builder()
                .javaParameterName(parameter.getName())
                .urlParameterName(pathVariable)
                .build();
    }
}
