package stevi.spring.web.servlet.handlermethod;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Parameter;

@Builder
@Data
public class MethodParameter {

    private Parameter parameter;

    private Object actualValue;
}
