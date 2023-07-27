package stevi.spring.web.context;

import lombok.Setter;
import stevi.spring.core.config.Config;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.context.BeanCache;
import stevi.spring.core.factory.BeanFactory;
import stevi.spring.web.annotations.Controller;

import java.util.List;
import java.util.Set;

public class WebApplicationContext implements ApplicationContext {

    @Setter
    private BeanFactory webBeanFactory;

    private final Config config;

    private final BeanCache webBeanCache;

    private final ApplicationContext autowiredApplicationContext;

    public WebApplicationContext(Config config, ApplicationContext autowiredApplicationContext) {
        this.config = config;
        this.autowiredApplicationContext = autowiredApplicationContext;
        this.webBeanCache = new BeanCache();
    }

    @Override
    public void postInit() {
        Set<Class<?>> annotatedClasses = config.getReflectionsScanner().getTypesAnnotatedWith(Controller.class);
        annotatedClasses.forEach(this::getWebBean);
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

    public <T> T getWebBean(Class<T> aClass) {
        if (webBeanCache.contains(aClass)) {
            return (T) webBeanCache.get(aClass);
        }

        T bean = webBeanFactory.createBeanWithoutProxy(aClass);
        putBeanIntoCache(aClass, bean);

        return bean;
    }

    private <T> void putBeanIntoCache(Class<? extends T> implClass, T object) {
        webBeanCache.put(implClass, object);
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public List<Object> getAllWebBeans() {
        return webBeanCache.getValues();
    }
}
