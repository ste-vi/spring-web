package stevi.spring.web.servlet;

import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.servlet.responsebuilder.HtmlPageResponseBuilder;
import stevi.spring.web.servlet.responsebuilder.JsonResponseBuilder;
import stevi.spring.web.servlet.responsebuilder.ResponseBuilder;
import stevi.spring.web.servlet.view.HtmlPageView;
import stevi.spring.web.servlet.view.JsonView;
import stevi.spring.web.servlet.view.View;

/**
 * Class which resolves view response builder based on view.
 */
public class ViewResolver {

    private final WebApplicationContext webApplicationContext;

    public ViewResolver(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    /**
     * Returns {@link ResponseBuilder} based on incoming {@link View}
     */
    public ResponseBuilder resolve(View view) {
        if (view instanceof HtmlPageView) {
            return webApplicationContext.getWebBean(HtmlPageResponseBuilder.class);
        } else if (view instanceof JsonView) {
            return webApplicationContext.getWebBean(JsonResponseBuilder.class);
        } else {
            throw new RuntimeException("View not found");
        }
    }

    /**
     * Returns {@link ResponseBuilder} based on incoming {@link View} for rendering exceptions.
     */
    public ResponseBuilder resolveForExceptions() {
        return webApplicationContext.getWebBean(JsonResponseBuilder.class);
    }
}
