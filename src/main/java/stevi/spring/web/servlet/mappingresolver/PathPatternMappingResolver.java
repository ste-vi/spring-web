package stevi.spring.web.servlet.mappingresolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.RequestBody;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.exceptions.RequestPathHandlerNotFoundException;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;
import stevi.spring.web.servlet.handlermethod.MethodParameter;
import stevi.spring.web.servlet.mappingresolver.pathcontext.MethodParameterPathContext;
import stevi.spring.web.servlet.mappingresolver.pathcontext.MethodPathContext;
import stevi.spring.web.servlet.mappingresolver.util.PathContextBuilderUtil;
import stevi.spring.web.servlet.mappingresolver.util.UrlPathToControllerPathMatcherUtil;
import stevi.spring.web.util.StreamUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

public class PathPatternMappingResolver implements MappingResolver {

    private final Map<MethodPathContext, Object> requestPathToControllerMap;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PathPatternMappingResolver(WebApplicationContext applicationContext) {
        requestPathToControllerMap = PathContextBuilderUtil.initMethodPathContextToControllerMap(applicationContext.getAllWebBeans());
    }

    @Override
    public HandlerMethod resolve(HttpServletRequest request) {
        return requestPathToControllerMap.entrySet()
                .stream()
                .filter(entry -> request.getMethod().equals(entry.getKey().getHttpMethod().name()))
                .filter(entry -> UrlPathToControllerPathMatcherUtil.isUrlMatchController(request.getPathInfo(), entry.getKey().getMethodFullPath()))
                .map(entry -> buildHandlerMethod(request, request.getPathInfo(), entry.getKey(), entry.getValue()))
                .findFirst()
                .orElseThrow(() -> new RequestPathHandlerNotFoundException("No http method handler found for path: %s".formatted(request.getPathInfo())));
    }

    private HandlerMethod buildHandlerMethod(HttpServletRequest request,
                                             String requestPathInfo,
                                             MethodPathContext methodPathContext,
                                             Object controller) {
        List<MethodParameter> methodParameters = initMethodParameters(request, requestPathInfo, methodPathContext);
        return HandlerMethod.builder()
                .method(methodPathContext.getMethod())
                .bean(controller)
                .methodParameters(methodParameters)
                .build();
    }

    private List<MethodParameter> initMethodParameters(HttpServletRequest request,
                                                       String requestPathInfo,
                                                       MethodPathContext methodPathContext) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Method method = methodPathContext.getMethod();

        List<MethodParameter> methodParameters = new ArrayList<>();
        methodParameters.addAll(getMethodParametersForPathVariables(requestPathInfo, methodPathContext, method));
        methodParameters.addAll(getMethodParametersForRequestParams(parameterMap, methodPathContext, method));
        methodParameters.addAll(getMethodParametersForRequestBody(request, methodPathContext, method));
        methodParameters.addAll(getMethodParametersWithoutAnyAnnotation(method, methodParameters));

        methodParameters.sort(Comparator.comparing(p -> p.getParameter().getName()));

        return methodParameters;
    }

    private List<MethodParameter> getMethodParametersForPathVariables(String requestPathInfo,
                                                                      MethodPathContext methodFullPath,
                                                                      Method method) {
        List<MethodParameter> methodParameters = new ArrayList<>();

        List<String> requestPathParts = Arrays.stream(requestPathInfo.split("/")).toList();
        List<String> controllerPathParts = Arrays.stream(methodFullPath.getMethodFullPath().split("/")).toList();

        for (int i = 0; i < requestPathParts.size(); i++) {
            String requestString = requestPathParts.get(i);
            String controllerString = controllerPathParts.get(i);

            if (UrlPathToControllerPathMatcherUtil.isControllerPathPartStringExpectsIdParameter(controllerString)) {
                String controllerPathFormattedString = controllerString.replace("{", "") + controllerString.replace("}", "");

                Parameter matchedPathVariableParam = Arrays.stream(method.getParameters())
                        .filter(param -> param.isAnnotationPresent(PathVariable.class))
                        .filter(param -> controllerPathFormattedString.equals(param.getAnnotation(PathVariable.class).value()))
                        .findFirst()
                        .orElse(null);

                MethodParameter methodParameter = MethodParameter
                        .builder().parameter(matchedPathVariableParam)
                        .actualValue(requestString)
                        .build();

                methodParameters.add(methodParameter);
            }
        }

        return methodParameters;
    }

    private List<MethodParameter> getMethodParametersForRequestParams(Map<String, String[]> parameterMap,
                                                                      MethodPathContext methodPathContext,
                                                                      Method method) {
        List<MethodParameter> methodParameters = new ArrayList<>();
        Map<String, Parameter> methodParametersMap = Arrays.stream(method.getParameters()).collect(toMap(Parameter::getName, p -> p));

        for (var pathContext : methodPathContext.getMethodParameterPathContexts()) {
            String[] queryParameterValues = parameterMap.get(pathContext.getUrlParameterName());
            if (queryParameterValues != null && queryParameterValues.length == 1) {
                String parameterValue = parameterMap.get(pathContext.getUrlParameterName())[0];
                Parameter parameter = methodParametersMap.get(pathContext.getJavaParameterName());

                MethodParameter methodParameter = MethodParameter
                        .builder().parameter(parameter)
                        .actualValue(parameterValue)
                        .build();

                methodParameters.add(methodParameter);
            }
        }
        return methodParameters;
    }

    @SneakyThrows
    private List<MethodParameter> getMethodParametersForRequestBody(HttpServletRequest request, MethodPathContext pathContext, Method method) {

        for (Parameter parameter : method.getParameters()) {
            Annotation[] annotations = parameter.getAnnotations();
            for (var annotation : annotations) {
                if (annotation instanceof RequestBody) {
                    Optional<MethodParameterPathContext> requestBodyOptional = pathContext.getMethodParameterPathContexts()
                            .stream()
                            .filter(parameterPathContext -> parameterPathContext.getJavaParameterName().equals(parameter.getName()))
                            .findFirst();

                    if (requestBodyOptional.isPresent()) {
                        MethodParameterPathContext parameterPathContext = requestBodyOptional.get();

                        byte[] requestBody = StreamUtils.convertToByteArray(request.getInputStream());
                        Class<?> javaParameterType = parameterPathContext.getJavaParameterType();
                        Object object = objectMapper.readValue(requestBody, javaParameterType);

                        MethodParameter methodParameter = MethodParameter.builder()
                                .parameter(parameter)
                                .actualValue(object)
                                .build();

                        return List.of(methodParameter);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private List<MethodParameter> getMethodParametersWithoutAnyAnnotation(Method method, List<MethodParameter> methodParameters) {
        if (methodParameters.size() != method.getParameterCount()) {
            return List.of();
        }
        return Arrays.stream(method.getParameters())
                .filter(param -> methodParameters.stream()
                        .filter(methodParameter -> param.equals(methodParameter.getParameter()))
                        .findAny()
                        .isEmpty())
                .map(param -> MethodParameter
                        .builder().parameter(param)
                        .actualValue(null)
                        .build())
                .toList();
    }

}
