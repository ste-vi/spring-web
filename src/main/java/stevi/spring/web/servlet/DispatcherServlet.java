package stevi.spring.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stevi.spring.core.config.DefaultConfig;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class DispatcherServlet extends FrameworkServlet {

    private final WebApplicationContext webApplicationContext = new WebApplicationContext(new DefaultConfig("stevi.spring"));
    private final Map<String, Object> requestPathToControllerMap;
    private final ObjectMapper objectMapper;

    public DispatcherServlet() {
        requestPathToControllerMap = webApplicationContext.getAllControllerBeans().stream()
                .filter(controller -> controller.getClass().isAnnotationPresent(RequestMapping.class))
                .collect(toMap(this::getRequestPathFromControllerClass, controller -> controller));

        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doDispatch(request, response);
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resultBodyString = "{\"message\": \"Hello from DispatcherServlet!\"}";

        if (requestPathToControllerMap.containsKey(request.getPathInfo())) {
            Object controller = requestPathToControllerMap.get(request.getPathInfo());
            for (Method declaredMethod : controller.getClass().getDeclaredMethods()) {
                if ("GET".equals(request.getMethod())) {
                    if (declaredMethod.isAnnotationPresent(GetMapping.class)) {
                        Object result = declaredMethod.invoke(controller);
                        Class<?> returnType = declaredMethod.getReturnType();
                        resultBodyString = objectMapper.writeValueAsString(result);
                    }
                }
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultBodyString);
    }

    private String getRequestPathFromControllerClass(Object controller) {
        RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
        String requestPath = requestMapping.value();

        if (requestPath.isBlank()) {
            requestPath = "/";
        }
        return requestPath;
    }
}
