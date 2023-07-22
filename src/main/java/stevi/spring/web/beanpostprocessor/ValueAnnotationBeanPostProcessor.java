package stevi.spring.web.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.core.anotation.Value;
import stevi.spring.core.beanpostprocessor.BeanPostProcessor;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.env.Environment;

/**
 * BeanPostProcessor for enabling fetching properties from property files and injecting into the bean fields.
 */
public class ValueAnnotationBeanPostProcessor implements BeanPostProcessor {

    /**
     * Checks bean declared fields and finds {@link Value} annotation.
     * <br>
     * Gets a property from environment properties by field name and insert as value.
     * <br>
     * If no property is found, gets default value from annotation value field.
     */
    @SneakyThrows
    @Override
    public void postProcessBeforeInitialization(Object bean, ApplicationContext applicationContext) {
        for (var declaredField : bean.getClass().getDeclaredFields()) {
            Value valueAnnotation = declaredField.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                Environment environment = applicationContext.getBean(Environment.class);
                String value = valueAnnotation.value().isEmpty() ? environment.getProperty(declaredField.getName()) : valueAnnotation.value();

                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        }
    }

    @Override
    public void postProcessAfterInitialization(Object bean, ApplicationContext applicationContext) {

    }
}
