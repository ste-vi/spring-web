package stevi.spring.web.handler;

import java.util.List;

public final class MethodParameterCastUtil {

    public static Object[] getCastedMethodParameterValues(List<MethodParameter> methodParameters) {
        return methodParameters
                .stream()
                .map(methodParameter -> {
                    String actualValue = methodParameter.getActualValue();
                    Class<?> aClass = methodParameter.getParameter().getType();

                    if (aClass == Integer.class) {
                        return Integer.valueOf(actualValue);
                    } else if (aClass == Long.class) {
                        return Long.valueOf(actualValue);
                    } else if (aClass == String.class) {
                        return actualValue;
                    }

                    return null;
                })
                .toArray();
    }

}
