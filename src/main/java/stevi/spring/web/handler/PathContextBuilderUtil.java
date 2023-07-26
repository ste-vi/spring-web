package stevi.spring.web.handler;

import stevi.spring.web.annotations.PostMapping;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.RequestBody;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.RequestParam;
import stevi.spring.web.handler.pathcontext.MethodParameterPathContext;
import stevi.spring.web.handler.pathcontext.MethodPathContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PathContextBuilderUtil {

    public static Map<MethodPathContext, Object> initMethodPathContextToControllerMap(List<Object> controllers) {

        Map<MethodPathContext, Object> methodPathContextToControllerMap = new HashMap<>();

        controllers.stream()
                .filter(controller -> controller.getClass().isAnnotationPresent(RequestMapping.class))
                .forEach(controller -> {
                    Class<?> controllerClass = controller.getClass();
                    RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);

                    for (var method : controllerClass.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(PostMapping.class)) {
                            MethodPathContext methodPathContext = initMethodPathContext(requestMapping, method);
                            methodPathContextToControllerMap.put(methodPathContext, controller);
                        }
                    }
                });


        return methodPathContextToControllerMap;
    }

    private static MethodPathContext initMethodPathContext(RequestMapping requestMapping, Method method) {
        PostMapping getMapping = method.getAnnotation(PostMapping.class);

        String methodMath = getMapping.value().startsWith("/") ? getMapping.value().substring(1) : getMapping.value();

        String fullMethodPath;
        if (methodMath.isEmpty()) {
            fullMethodPath = requestMapping.value();
        } else {
            String requestMappingPath = requestMapping.value().endsWith("/") ? requestMapping.value() : requestMapping.value() + "/";
            fullMethodPath = requestMappingPath + methodMath;
        }

        return MethodPathContext.builder()
                .methodFullPath(fullMethodPath)
                .method(method)
                .methodParameterPathContexts(getMethodParameterPathContexts(method))
                .build();
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
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                MethodParameterPathContext parameterPathContext = getParameterPathContext(parameter.getName(), parameter);
                parameterPathContext.setJavaParameterType(parameter.getType());
                methodParameterPathContexts.add(parameterPathContext);
            }
        }
        return methodParameterPathContexts;
    }

    private static MethodParameterPathContext getParameterPathContext(String urlParam, Parameter parameter) {
        return MethodParameterPathContext.builder()
                .javaParameterName(parameter.getName())
                .urlParameterName(urlParam)
                .build();
    }
}
