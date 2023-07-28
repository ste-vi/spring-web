package stevi.spring.web.servlet.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import stevi.spring.web.annotations.ResponseStatus;
import stevi.spring.web.http.HttpStatus;
import stevi.spring.web.servlet.ModelAndView;
import stevi.spring.web.servlet.handler.util.MethodParameterCastUtil;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;
import stevi.spring.web.servlet.view.JsonView;

import java.lang.reflect.Method;

/**
 * Supports the invocation of @RequestMapping handler methods.
 */
@RequiredArgsConstructor
public class ControllerMappingHandler implements Handler {

    /**
     * Applies method execution and returns model and view object.
     *
     * @param handlerMethod handler method and additional info for execution
     * @return {@link ModelAndView} object
     */
    @SneakyThrows
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
        Object[] params = MethodParameterCastUtil.getCastedMethodParameterValues(handlerMethod.getMethodParameters());
        Method method = handlerMethod.getMethod();

        Object model = method.invoke(handlerMethod.getBean(), params);
        HttpStatus status = getHttpStatus(method);

        return ModelAndView.builder()
                .httpStatus(status)
                .model(model)
                .view(new JsonView())
                .build();
    }

    private HttpStatus getHttpStatus(Method method) {
        return method.isAnnotationPresent(ResponseStatus.class)
                ? method.getAnnotation(ResponseStatus.class).value()
                : HttpStatus.OK;
    }
}
