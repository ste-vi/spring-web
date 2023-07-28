package stevi.spring.web.servlet.handlermethod;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Parameter;

/**
 * POJO which contains needed parameters data for handler method execution.
 */
@Builder
@Data
public class MethodParameter {

    private Parameter parameter;

    private Object actualValue;
}
