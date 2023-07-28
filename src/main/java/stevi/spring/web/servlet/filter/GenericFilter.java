package stevi.spring.web.servlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;

/**
 * Simple base implementation of Filter.
 * Inheritors should add {@code @WebFilter(urlPatterns = "*")} to register filters in servlet context.
 */
public abstract class GenericFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
