package stevi.spring.web.servlet;

import lombok.Builder;
import lombok.Data;
import stevi.spring.web.http.HttpStatus;
import stevi.spring.web.servlet.view.View;

/**
 * Holder for both Model and View in the web MVC framework. Note that these are entirely distinct. </br>
 * This class merely holds both to make it possible for a controller to return both model and view in a single return value. </br>
 * Represents a model and view returned by a handler, to be resolved by a DispatcherServlet.
 */
@Builder
@Data
public class ModelAndView {

    private Object model;

    private View view;

    private HttpStatus httpStatus;
}
