package stevi.spring.web.servlet.mappingresolver.util;

import stevi.spring.web.annotations.DeleteMapping;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.PatchMapping;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.PostMapping;
import stevi.spring.web.annotations.PutMapping;
import stevi.spring.web.annotations.RequestBody;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.RequestParam;
import stevi.spring.web.http.HttpMethod;
import stevi.spring.web.servlet.mappingresolver.pathcontext.MethodParameterPathContext;
import stevi.spring.web.servlet.mappingresolver.pathcontext.MethodPathContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class PathContextBuilderUtil {

    public static Map<MethodPathContext, Object> initMethodPathContextToControllerMap(List<Object> controllers) {
        Map<MethodPathContext, Object> methodPathContextToControllerMap = new HashMap<>();
        controllers.stream()
                .filter(controller -> controller.getClass().isAnnotationPresent(RequestMapping.class))
                .forEach(controller -> initMap(methodPathContextToControllerMap, controller));
        return methodPathContextToControllerMap;
    }

    private static void initMap(Map<MethodPathContext, Object> methodPathContextToControllerMap, Object controller) {
        Class<?> controllerClass = controller.getClass();
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        String requestMappingPath = requestMapping.value();

        for (var method : controllerClass.getDeclaredMethods()) {
            Optional<MethodPathContext> methodPathContextOptional = getMethodPathContext(requestMappingPath, method);
            methodPathContextOptional.ifPresent(methodPathContext -> methodPathContextToControllerMap.put(methodPathContext, controller));
        }
    }

    private static Optional<MethodPathContext> getMethodPathContext(String requestMappingPath, Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            String httpMethodPath = method.getAnnotation(GetMapping.class).value();
            MethodPathContext methodPathContext = initMethodPathContext(requestMappingPath, httpMethodPath, HttpMethod.GET, method);
            return Optional.of(methodPathContext);
        }
        if (method.isAnnotationPresent(PostMapping.class)) {
            String httpMethodPath = method.getAnnotation(PostMapping.class).value();
            MethodPathContext methodPathContext = initMethodPathContext(requestMappingPath, httpMethodPath, HttpMethod.POST, method);
            return Optional.of(methodPathContext);
        }
        if (method.isAnnotationPresent(PutMapping.class)) {
            String httpMethodPath = method.getAnnotation(PutMapping.class).value();
            MethodPathContext methodPathContext = initMethodPathContext(requestMappingPath, httpMethodPath, HttpMethod.PUT, method);
            return Optional.of(methodPathContext);
        }
        if (method.isAnnotationPresent(PatchMapping.class)) {
            String httpMethodPath = method.getAnnotation(PatchMapping.class).value();
            MethodPathContext methodPathContext = initMethodPathContext(requestMappingPath, httpMethodPath, HttpMethod.PATCH, method);
            return Optional.of(methodPathContext);
        }
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            String httpMethodPath = method.getAnnotation(DeleteMapping.class).value();
            MethodPathContext methodPathContext = initMethodPathContext(requestMappingPath, httpMethodPath, HttpMethod.DELETE, method);
            return Optional.of(methodPathContext);
        }
        return Optional.empty();
    }

    private static MethodPathContext initMethodPathContext(String requestMappingPath,
                                                           String httpMethodPath,
                                                           HttpMethod httpMethod,
                                                           Method method) {
        httpMethodPath = formatHttpMethodPath(httpMethodPath);
        String fullMethodPath = getFullMethodPath(requestMappingPath, httpMethodPath);
        List<MethodParameterPathContext> methodParameterPathContexts = getMethodParameterPathContexts(method);

        return MethodPathContext.builder()
                .method(method)
                .methodFullPath(fullMethodPath)
                .httpMethod(httpMethod)
                .methodParameterPathContexts(methodParameterPathContexts)
                .build();
    }

    private static String getFullMethodPath(String requestMappingPath, String httpMethodPath) {
        if (httpMethodPath.isEmpty()) {
            return requestMappingPath;
        } else {
            requestMappingPath = formatRequestMethodPath(requestMappingPath);
            return requestMappingPath + httpMethodPath;
        }
    }

    private static String formatHttpMethodPath(String httpMethodPath) {
        httpMethodPath = httpMethodPath.startsWith("/") ? httpMethodPath.substring(1) : httpMethodPath;
        return httpMethodPath;
    }

    private static String formatRequestMethodPath(String requestMappingPath) {
        return requestMappingPath.endsWith("/") ? requestMappingPath : requestMappingPath + "/";
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
                methodParameterPathContexts.add(parameterPathContext);
            }
        }
        return methodParameterPathContexts;
    }

    private static MethodParameterPathContext getParameterPathContext(String urlParam, Parameter parameter) {
        return MethodParameterPathContext.builder()
                .javaParameterName(parameter.getName())
                .javaParameterType(parameter.getType())
                .urlParameterName(urlParam)
                .build();
    }
}
