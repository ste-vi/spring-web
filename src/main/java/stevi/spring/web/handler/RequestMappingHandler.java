package stevi.spring.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import stevi.spring.web.annotations.ResponseStatus;
import stevi.spring.web.http.HttpStatus;
import stevi.spring.web.servlet.ModelAndView;
import stevi.spring.web.servlet.view.JsonView;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class RequestMappingHandler implements Handler {

    @SneakyThrows
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        Object model = method.invoke(handlerMethod.getBean());

        HttpStatus status = method.isAnnotationPresent(ResponseStatus.class)
                ? method.getAnnotation(ResponseStatus.class).value()
                : HttpStatus.OK;

        return ModelAndView.builder()
                .model(model)
                .view(new JsonView())
                .httpStatus(status)
                .build();
    }
}
