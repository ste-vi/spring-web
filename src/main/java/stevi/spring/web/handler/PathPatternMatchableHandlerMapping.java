package stevi.spring.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.handler.pathcontext.ControllerPathContext;
import stevi.spring.web.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class PathPatternMatchableHandlerMapping implements HandlerMapping {

    private final Map<ControllerPathContext, Object> requestPathToControllerMap;

    public PathPatternMatchableHandlerMapping(WebApplicationContext applicationContext) {
        requestPathToControllerMap = initRequestPathToControllerMap(applicationContext);
    }

    private Map<ControllerPathContext, Object> initRequestPathToControllerMap(WebApplicationContext applicationContext) {
        return applicationContext.getAllControllerBeans().stream()
                .filter(controller -> controller.getClass().isAnnotationPresent(RequestMapping.class))
                .collect(toMap(ControllerPathContextBuilderUtil::getRequestPathFromControllerClass, controller -> controller));
    }

    @Override
    public HandlerMethod resolveHandlerMethodFromRequestPath(HttpServletRequest request) {

        String requestPathInfo = request.getPathInfo();
        Map<String, String[]> requestParameterMap = request.getParameterMap();


        requestPathToControllerMap.entrySet()
                .stream()
                .filter(entry -> {
                    ControllerPathContext key = entry.getKey();


                    if(requestPathInfo.startsWith(key.getRequestMappingPath())) {
                        key.getMethodPathContexts()
                                .stream()
                                .filter(methodPathContext -> methodPathContext.getMethodPath())
                    }


                })

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

    // /users
    // /users/{id}
    // users/profile
    // users/profile/{id}
    // users/{id}/profile
    // users/{id}/profile?name=2

    // how to not apply proxy?


}
