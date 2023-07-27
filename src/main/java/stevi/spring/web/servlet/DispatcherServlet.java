package stevi.spring.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.exceptions.ExceptionResponse;
import stevi.spring.web.exceptions.FrameworkException;
import stevi.spring.web.servlet.handler.ControllerMappingHandler;
import stevi.spring.web.servlet.mappingresolver.MappingResolver;
import stevi.spring.web.servlet.mappingresolver.PathPatternMappingResolver;
import stevi.spring.web.servlet.handler.Handler;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;
import stevi.spring.web.servlet.responsebuilder.ResponseBuilder;

public class DispatcherServlet extends FrameworkServlet {

    private final MappingResolver mappingResolver;
    private final Handler handler;
    private final ViewResolver viewResolver;

    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        this.mappingResolver = new PathPatternMappingResolver(webApplicationContext);
        this.handler = new ControllerMappingHandler();
        this.viewResolver = new ViewResolver(webApplicationContext);
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) {
        try {
            doDispatch(request, response);
        } catch (FrameworkException exception) {
            ExceptionResponse exceptionResponse = exception.getExceptionResponse();
            ResponseBuilder responseBuilder = viewResolver.resolveForExceptions();
            responseBuilder.buildResponse(exceptionResponse, exceptionResponse.statusCode(), response);
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        HandlerMethod handlerMethod = mappingResolver.resolve(request);
        ModelAndView modelAndView = handler.handle(request, response, handlerMethod);
        ResponseBuilder responseBuilder = viewResolver.resolve(modelAndView.getView());
        responseBuilder.buildResponse(modelAndView.getModel(), modelAndView.getHttpStatus().getValue(), response);
    }
}
