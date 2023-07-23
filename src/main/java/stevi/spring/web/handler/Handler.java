package stevi.spring.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stevi.spring.web.servlet.ModelAndView;

public interface Handler {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod);
}
