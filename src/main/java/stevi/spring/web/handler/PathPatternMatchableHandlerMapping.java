package stevi.spring.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.handler.pathcontext.MethodPathContext;
import stevi.spring.web.http.HttpMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class PathPatternMatchableHandlerMapping implements HandlerMapping {

    private final Map<MethodPathContext, Object> requestPathToControllerMap;

    public PathPatternMatchableHandlerMapping(WebApplicationContext applicationContext) {
        requestPathToControllerMap = PathContextBuilderUtil.initMethodPathContextToControllerMap(applicationContext.getAllControllerBeans());
    }

    @Override
    public HandlerMethod resolveHandlerMethodFromRequestPath(HttpServletRequest request) {
        HandlerMethod handlerMethod = null;

        String requestPathInfo = request.getPathInfo();
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<MethodPathContext, Object> entry : requestPathToControllerMap.entrySet()) {
            MethodPathContext methodFullPath = entry.getKey();
            Object controller = entry.getValue();
            boolean isUrlMatch = isUrlMatchController(requestPathInfo, methodFullPath.getMethodFullPath());
            if (isUrlMatch) {
                Method method = methodFullPath.getMethod();
                Map<String, Parameter> methodParametersMap = Arrays.stream(method.getParameters()).collect(toMap(Parameter::getName, p -> p));

                List<MethodParameter> methodParameters = methodFullPath.getMethodParameterPathContexts()
                        .stream()
                        .filter(pathContext -> {
                            String[] queryParameterValues = parameterMap.get(pathContext.getUrlParameterName());
                            return queryParameterValues != null && queryParameterValues.length == 1;
                        })
                        .map(pathContext -> {
                            String parameterValue = parameterMap.get(pathContext.getUrlParameterName())[0];
                            Parameter parameter = methodParametersMap.get(pathContext.getJavaParameterName());
                            return MethodParameter
                                    .builder().parameter(parameter)
                                    .actualValue(parameterValue)
                                    .build();
                        })
                        .toList();

                if (HttpMethod.GET.name().equals(request.getMethod()) && method.isAnnotationPresent(GetMapping.class)) {
                    handlerMethod = HandlerMethod.builder()
                            .method(method)
                            .bean(controller)
                            .beanType(controller.getClass())
                            .methodParameters(methodParameters)
                            .build();
                    break;
                }
            }
        }

        if (handlerMethod == null) {
            throw new RuntimeException("No method handler found for path: %s".formatted(requestPathInfo));
        }

        return handlerMethod;
    }

    private boolean isUrlMatchController(String requestPath, String controllerPath) {
        List<String> requestPathParts = Arrays.stream(requestPath.split("/")).toList();
        List<String> controllerPathParts = Arrays.stream(controllerPath.split("/")).toList();

        boolean isMatch = false;

        for (int i = 0; i < requestPathParts.size(); i++) {
            String requestString = requestPathParts.get(i);
            String controllerString = controllerPathParts.get(i);

            boolean stringsAreEqual = requestString.equals(controllerString);
            boolean numbersAreEqual = isRequestPathPartStringNumber(requestString) && isControllerPathPartStringExpectsIdParameter(controllerString);

            if (stringsAreEqual || numbersAreEqual) {
                isMatch = true;
            } else {
                isMatch = false;
                break;
            }
        }
        return isMatch;
    }

    private boolean isRequestPathPartStringNumber(String requestString) {
        try {
            return Integer.parseInt(requestString) > -1 || Long.parseLong(requestString) > -1;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private boolean isControllerPathPartStringExpectsIdParameter(String controllerString) {
        return controllerString.startsWith("{") && controllerString.endsWith("}");
    }

}
