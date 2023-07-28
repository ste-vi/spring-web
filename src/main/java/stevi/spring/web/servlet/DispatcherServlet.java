package stevi.spring.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.exceptions.ExceptionResponse;
import stevi.spring.web.exceptions.FrameworkException;
import stevi.spring.web.servlet.handler.ControllerMappingHandler;
import stevi.spring.web.servlet.handler.Handler;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;
import stevi.spring.web.servlet.mappingresolver.MappingResolver;
import stevi.spring.web.servlet.mappingresolver.PathPatternMappingResolver;
import stevi.spring.web.servlet.responsebuilder.ResponseBuilder;

/**
 * Central dispatcher for HTTP request handlers/controllers, e.g. for web UI controllers or HTTP-based remote service exporters.
 * Dispatches to registered handlers for processing a web request, providing convenient mapping and exception handling facilities.
 */
public class DispatcherServlet extends FrameworkServlet {

    private final MappingResolver mappingResolver;
    private final Handler handler;
    private final ViewResolver viewResolver;

    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        this.mappingResolver = new PathPatternMappingResolver(webApplicationContext);
        this.handler = new ControllerMappingHandler();
        this.viewResolver = new ViewResolver(webApplicationContext);
    }

    /**
     * Exposes the DispatcherServlet-specific request attributes and delegates to doDispatch for the actual dispatching.
     */
    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) {
        try {
            doDispatch(request, response);
        } catch (FrameworkException exception) {
            doDispatchException(response, exception);
        }
    }

    /**
     * Resolves handler method from url request path.
     * Handles method (executes actual controller method) and returns {@link ModelAndView}.
     * Resolved view.
     * Build response based on view;
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        HandlerMethod handlerMethod = mappingResolver.resolve(request);
        ModelAndView modelAndView = handler.handle(request, response, handlerMethod);
        ResponseBuilder responseBuilder = viewResolver.resolve(modelAndView.getView());
        responseBuilder.buildResponse(modelAndView.getModel(), modelAndView.getHttpStatus().getValue(), response);
    }

    private void doDispatchException(HttpServletResponse response, FrameworkException exception) {
        ExceptionResponse exceptionResponse = exception.getExceptionResponse();
        ResponseBuilder responseBuilder = viewResolver.resolveForExceptions();
        responseBuilder.buildResponse(exceptionResponse, exceptionResponse.statusCode(), response);
    }
}
