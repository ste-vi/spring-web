package stevi.spring.web.servlet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Filter implementation to log incoming requests.
 */
@Slf4j
@WebFilter(urlPatterns = "*")
public class DefaultLoggingFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.debug("Received new request with method {} and path: {}", httpServletRequest.getMethod(), httpServletRequest.getPathInfo());
        chain.doFilter(request, response);
    }
}
