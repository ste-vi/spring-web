package stevi.spring.web.servlet.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;
import stevi.spring.web.servlet.ModelAndView;

public interface Handler {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod);
}
