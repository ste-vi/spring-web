package stevi.spring.web.servlet.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stevi.spring.web.servlet.handlermethod.HandlerMethod;
import stevi.spring.web.servlet.ModelAndView;

/**
 * Interface which is responsible for execution of target handled method and returning {@link ModelAndView} result.
 */
public interface Handler {

    /**
     * Applies method execution and returns model and view object.
     *
     * @param handlerMethod handler method and additional info for execution
     * @return {@link ModelAndView} object
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod);
}
