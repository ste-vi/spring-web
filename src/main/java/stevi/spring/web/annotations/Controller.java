package stevi.spring.web.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a "Controller" (e.g. a web controller).<br>
 * This annotation serves as a specialization of @Component,
 * allowing for implementation classes to be autodetected through classpath scanning.<br>
 * It is typically used in combination with annotated handler methods based on the org.springframework.web.bind.annotation.RequestMapping annotation.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
}
