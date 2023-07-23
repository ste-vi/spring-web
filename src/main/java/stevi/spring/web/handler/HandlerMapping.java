package stevi.spring.web.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    HandlerMethod resolveHandlerMethodFromRequestPath(HttpServletRequest request);

}
