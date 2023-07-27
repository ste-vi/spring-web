package stevi.spring.web.servlet.mappingresolver;

import jakarta.servlet.http.HttpServletRequest;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;

public interface MappingResolver {

    HandlerMethod resolve(HttpServletRequest request);

}
