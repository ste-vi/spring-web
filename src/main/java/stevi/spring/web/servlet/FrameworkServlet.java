package stevi.spring.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public abstract class FrameworkServlet extends HttpServlet {

    /**
     * HTTP methods supported by {@link jakarta.servlet.http.HttpServlet}.
     */
    private static final Set<String> HTTP_SERVLET_METHODS = Set.of("DELETE", "HEAD", "GET", "OPTIONS", "POST", "PUT", "TRACE");

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (HTTP_SERVLET_METHODS.contains(request.getMethod())) {
            super.service(request, response);
        } else {
            processRequest(request, response);
        }
    }

    /**
     * Delegate GET requests to processRequest/doService.
     * <p>Will also be invoked by HttpServlet's default implementation of {@code doHead},
     * with a {@code NoBodyResponse} that just captures the content length.
     *
     * @see #doService
     * @see #doHead
     */
    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            doService(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void doService(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
