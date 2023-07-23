package stevi.spring.web.context;

import lombok.Setter;
import stevi.spring.core.config.Config;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.context.AutowiredApplicationContext;
import stevi.spring.core.context.BeanCache;
import stevi.spring.core.factory.BeanFactory;
import stevi.spring.test.TestController;
import stevi.spring.web.annotations.Controller;

import java.util.List;
import java.util.Set;

public class WebApplicationContext implements ApplicationContext {

    @Setter
    private BeanFactory controllerBeanFactory;

    private final Config config;

    private final BeanCache controllerBeanCache;

    private final ApplicationContext autowiredApplicationContext;

    // todo: how to inject services into controllers, since both contexts do not know about each other

    public WebApplicationContext(Config config, ApplicationContext autowiredApplicationContext) {
        this.config = config;
        this.autowiredApplicationContext = autowiredApplicationContext;
        this.controllerBeanCache = new BeanCache();
    }

    @Override
    public void postInit() {
        Set<Class<?>> annotatedClasses = config.getReflectionsScanner().getTypesAnnotatedWith(Controller.class);
        annotatedClasses.forEach(this::getControllerBean);
    }

    @Override
    public <T> T getBeanByName(String beanName) {
        return null;
    }

    /**
     * Fetches bean from autowired application context.
     *
     * @param aClass class to get bean based on
     */
    @Override
    public <T> T getBean(Class<T> aClass) {
        return autowiredApplicationContext.getBean(aClass);
    }

    public <T> T getControllerBean(Class<T> aClass) {
        if (controllerBeanCache.contains(aClass)) {
            return (T) controllerBeanCache.get(aClass);
        }

        T bean = controllerBeanFactory.createBeanWithoutProxy(aClass);
        putBeanIntoCache(aClass, bean);

        return bean;
    }

    private <T> void putBeanIntoCache(Class<? extends T> implClass, T object) {
        if (implClass.isAnnotationPresent(Controller.class)) {
            controllerBeanCache.put(implClass, object);
        }
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public List<Object> getAllControllerBeans() {
        return controllerBeanCache.getValues();
    }
}
