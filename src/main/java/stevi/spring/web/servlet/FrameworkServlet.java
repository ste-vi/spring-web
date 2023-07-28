package stevi.spring.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stevi.spring.web.http.HttpMethod;

import java.io.IOException;
import java.util.Set;

/**
 * Base servlet for Spring's web framework. Provides integration with a Spring application context, in a JavaBean-based overall solution.
 * This class offers the following functionality:
 * Manages a WebApplicationContext instance per servlet. The servlet's configuration is determined by beans in the servlet's namespace.
 * Publishes events on request processing, regardless a request is successfully handled.
 * Subclasses must implement doService to handle requests.
 */
public abstract class FrameworkServlet extends HttpServlet {

    /**
     * HTTP methods supported by {@link jakarta.servlet.http.HttpServlet}.
     */
    private static final Set<String> HTTP_SERVLET_METHODS = Set.of(HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.GET.name(),
            HttpMethod.OPTIONS.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.TRACE.name());


    /**
     * Override the parent class implementation in order to intercept requests using PATCH or non-standard HTTP methods (WebDAV).
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (HTTP_SERVLET_METHODS.contains(request.getMethod())) {
            super.service(request, response);
        } else {
            processRequest(request, response);
        }
    }

    /**
     * Delegate GET requests to processRequest/doService.
     */
    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Delegate POST requests to {@link #processRequest}.
     *
     * @see #doService
     */
    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Delegate PUT requests to {@link #processRequest}.
     *
     * @see #doService
     */
    @Override
    protected final void doPut(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Delegate DELETE requests to {@link #processRequest}.
     *
     * @see #doService
     */
    @Override
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Process this request, publishing an event regardless of the outcome.
     * <p>The actual event handling is performed by the abstract
     * {@link #doService} template method.
     */
    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            doService(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Subclasses must implement this method to do the work of request handling, receiving a centralized callback for GET, POST, PUT and DELETE.
     * The contract is essentially the same as that for the commonly overridden doGet or doPost methods of HttpServlet.
     * This class intercepts calls to ensure that exception handling and event publication takes place.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @throws Exception
     */
    protected abstract void doService(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
