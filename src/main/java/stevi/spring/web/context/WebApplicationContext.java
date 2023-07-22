package stevi.spring.web.context;

import stevi.spring.core.config.Config;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.context.BeanCache;
import stevi.spring.test.TestController;
import stevi.spring.web.annotations.Controller;

import java.util.List;
import java.util.Set;

public class WebApplicationContext implements ApplicationContext {

    private final Config config;

    private final BeanCache beanCache;

    public WebApplicationContext(Config config) {
        this.config = config;
        this.beanCache = new BeanCache();
        this.beanCache.put(TestController.class, new TestController());
    }

    @Override
    public void postInit() {
        Set<Class<?>> annotatedClasses = config.getReflectionsScanner().getTypesAnnotatedWith(Controller.class);
        annotatedClasses.forEach(this::getBean);
    }

    @Override
    public <T> T getBeanByName(String s) {
        return null;
    }

    /**
     * Fetches bean from cache.
     * Created new bean and sets into cache of not found.
     *
     * @param aClass class to get bean based on
     */
    @Override
    public <T> T getBean(Class<T> aClass) {
        Class<? extends T> implClass = getImplementationClass(aClass);
        if (beanCache.contains(implClass)) {
            return (T) beanCache.get(implClass);
        }

      /*  T bean = beanFactory.createBean(implClass);
        putBeanIntoCache(implClass, bean);*/

        return (T) new TestController();
    }

    private <T> Class<? extends T> getImplementationClass(Class<T> aClass) {
        Class<? extends T> implClass = aClass;
        if (implClass.isInterface()) {
            implClass = config.getImplementation(aClass);
        }
        return implClass;
    }

    private <T> void putBeanIntoCache(Class<? extends T> implClass, T object) {
        if (implClass.isAnnotationPresent(Controller.class)) {
            beanCache.put(implClass, object);
        }
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public List<Object> getAllControllerBeans() {
        return beanCache.getValues();
    }
}
