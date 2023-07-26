package stevi.spring.web.handler;

import stevi.spring.web.annotations.DeleteMapping;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.PatchMapping;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.PostMapping;
import stevi.spring.web.annotations.PutMapping;
import stevi.spring.web.annotations.RequestBody;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.RequestParam;
import stevi.spring.web.handler.pathcontext.MethodParameterPathContext;
import stevi.spring.web.handler.pathcontext.MethodPathContext;
import stevi.spring.web.http.HttpMethod;

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
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            String httpMethodPath = method.getAnnotation(GetMapping.class).value();
                            MethodPathContext methodPathContext = initMethodPathContext(requestMapping, httpMethodPath, method);
                            methodPathContext.setHttpMethod(HttpMethod.GET);
                            methodPathContextToControllerMap.put(methodPathContext, controller);
                        }
                        if (method.isAnnotationPresent(PostMapping.class)) {
                            String httpMethodPath = method.getAnnotation(PostMapping.class).value();
                            MethodPathContext methodPathContext = initMethodPathContext(requestMapping, httpMethodPath, method);
                            methodPathContext.setHttpMethod(HttpMethod.POST);
                            methodPathContextToControllerMap.put(methodPathContext, controller);
                        }
                        if (method.isAnnotationPresent(PutMapping.class)) {
                            String httpMethodPath = method.getAnnotation(PutMapping.class).value();
                            MethodPathContext methodPathContext = initMethodPathContext(requestMapping, httpMethodPath, method);
                            methodPathContext.setHttpMethod(HttpMethod.PUT);
                            methodPathContextToControllerMap.put(methodPathContext, controller);
                        }
                        if (method.isAnnotationPresent(PatchMapping.class)) {
                            String httpMethodPath = method.getAnnotation(PatchMapping.class).value();
                            MethodPathContext methodPathContext = initMethodPathContext(requestMapping, httpMethodPath, method);
                            methodPathContext.setHttpMethod(HttpMethod.PATCH);
                            methodPathContextToControllerMap.put(methodPathContext, controller);
                        }
                        if (method.isAnnotationPresent(DeleteMapping.class)) {
                            String httpMethodPath = method.getAnnotation(DeleteMapping.class).value();
                            MethodPathContext methodPathContext = initMethodPathContext(requestMapping, httpMethodPath, method);
                            methodPathContext.setHttpMethod(HttpMethod.DELETE);
                            methodPathContextToControllerMap.put(methodPathContext, controller);
                        }
                    }
                });

        return methodPathContextToControllerMap;
    }

    private static MethodPathContext initMethodPathContext(RequestMapping requestMapping, String httpMethodPath, Method method) {
        httpMethodPath = httpMethodPath.startsWith("/") ? httpMethodPath.substring(1) : httpMethodPath;

        String fullMethodPath;
        if (httpMethodPath.isEmpty()) {
            fullMethodPath = requestMapping.value();
        } else {
            String requestMappingPath = requestMapping.value().endsWith("/") ? requestMapping.value() : requestMapping.value() + "/";
            fullMethodPath = requestMappingPath + httpMethodPath;
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
