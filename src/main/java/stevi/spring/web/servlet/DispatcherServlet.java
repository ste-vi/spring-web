package stevi.spring.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import stevi.spring.core.config.DefaultConfig;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.handler.Handler;
import stevi.spring.web.handler.HandlerMapping;
import stevi.spring.web.handler.HandlerMethod;
import stevi.spring.web.handler.PathPatternMatchableHandlerMapping;
import stevi.spring.web.handler.RequestMappingHandler;
import stevi.spring.web.servlet.view.HtmlPageView;
import stevi.spring.web.servlet.view.JsonView;

import java.io.IOException;

public class DispatcherServlet extends FrameworkServlet {

    private final HandlerMapping handlerMapping;
    private final Handler handler;
    private final ObjectMapper objectMapper;

    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        this.handlerMapping = new PathPatternMatchableHandlerMapping(webApplicationContext);
        this.handler = new RequestMappingHandler();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doDispatch(request, response);
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        HandlerMethod handlerMethod = handlerMapping.resolveHandlerMethodFromRequestPath(request);
        ModelAndView modelAndView = handler.handle(request, response, handlerMethod);
        renderView(modelAndView, request, response);
    }

    @SneakyThrows
    private void renderView(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        if (modelAndView.getView() instanceof HtmlPageView) {
            renderHTMLPage();
        } else if (modelAndView.getView() instanceof JsonView) {
            renderJSONView(modelAndView, response);
        } else {
            throw new NotFoundException("View not found");
        }
    }

    private void renderHTMLPage() {
        throw new UnsupportedOperationException("HTML pages not yet supported, please return object to map into JSON");
    }

    private void renderJSONView(ModelAndView modelAndView, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(modelAndView.getHttpStatus().getValue());

        if (modelAndView.getModel() != null) {
            response.getWriter().write(objectMapper.writeValueAsString(modelAndView.getModel()));
        }
    }
}
