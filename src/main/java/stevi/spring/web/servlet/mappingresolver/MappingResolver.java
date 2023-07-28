package stevi.spring.web.servlet.mappingresolver;

import jakarta.servlet.http.HttpServletRequest;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;

/**
 * Interface to be implemented by objects that define a mapping between requests and handler objects.
 */
public interface MappingResolver {

    /**
     * Resolves controller handler method from http request.
     */
    HandlerMethod resolve(HttpServletRequest request);

}
