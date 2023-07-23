package stevi.spring.web.servlet;

import lombok.Builder;
import lombok.Data;
import stevi.spring.web.http.HttpStatus;
import stevi.spring.web.servlet.view.View;

@Builder
@Data
public class ModelAndView {

    private Object model;

    private View view;

    private HttpStatus httpStatus;
}
