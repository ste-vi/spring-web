package stevi.spring.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.http.HttpMethod;
import stevi.spring.web.http.RequestPath;

import java.lang.reflect.Method;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class PathPatternMatchableHandlerMapping implements HandlerMapping {

    private final Map<RequestPath, Object> requestPathToControllerMap;

    public PathPatternMatchableHandlerMapping(WebApplicationContext applicationContext) {
        requestPathToControllerMap = initRequestPathToControllerMap(applicationContext);
    }

    @Override
    public HandlerMethod resolveHandlerMethodFromRequestPath(HttpServletRequest request) {

        //if (requestPathToControllerMap.containsKey(request.getPathInfo())) {
        Object controller = requestPathToControllerMap.values().iterator().next();

        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (HttpMethod.GET.name().equals(request.getMethod()) && method.isAnnotationPresent(GetMapping.class)) {
                return HandlerMethod.builder()
                        .method(method)
                        .bean(controller)
                        .beanType(controller.getClass())
                        .parameters(method.getParameters())
                        .build();
            }
        }
        //}

        return null;
    }

    private Map<RequestPath, Object> initRequestPathToControllerMap(WebApplicationContext applicationContext) {
        final Map<RequestPath, Object> requestPathToControllerMap;
        requestPathToControllerMap = applicationContext.getAllControllerBeans().stream()
                .filter(controller -> controller.getClass().isAnnotationPresent(RequestMapping.class))
                .collect(toMap(this::getRequestPathFromControllerClass, controller -> controller));
        return requestPathToControllerMap;
    }

    private RequestPath getRequestPathFromControllerClass(Object controller) {
        RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
        String requestPath = requestMapping.value();

        if (requestPath.isBlank()) {
            requestPath = "/";
        }

        return RequestPath.builder().fullPath(requestPath).build();
    }
}
