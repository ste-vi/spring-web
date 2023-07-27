package stevi.spring.web.servlet.handler.util;

import stevi.spring.web.servlet.handlermethod.MethodParameter;

import java.util.List;

public final class MethodParameterCastUtil {

    public static Object[] getCastedMethodParameterValues(List<MethodParameter> methodParameters) {
        return methodParameters
                .stream()
                .map(methodParameter -> {
                    Object actualValue = methodParameter.getActualValue();
                    Class<?> aClass = methodParameter.getParameter().getType();

                    if (actualValue == null) {
                        return null;
                    }

                    if (aClass == Integer.class) {
                        return Integer.valueOf((String) actualValue);
                    } else if (aClass == Long.class) {
                        return Long.valueOf((String) actualValue);
                    } else if (aClass == String.class) {
                        return actualValue;
                    } else {
                        return actualValue;
                    }
                })
                .toArray();
    }

}
