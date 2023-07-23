package stevi.spring.web.context;

import stevi.spring.core.context.Application;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.factory.BeanFactory;
import stevi.spring.web.servlet.DispatcherServlet;
import stevi.spring.web.webserver.TomcatWebServer;

public class WebApplication {

    public static ApplicationContext run(String packagesToScan) {
        ApplicationContext applicationContext = Application.run(packagesToScan);

        WebApplicationContext webApplicationContext = createWebApplicationContext(applicationContext);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);

        TomcatWebServer tomcatStarter = new TomcatWebServer(dispatcherServlet);
        tomcatStarter.start();

        return applicationContext;
    }

    private static WebApplicationContext createWebApplicationContext(ApplicationContext applicationContext) {
        WebApplicationContext webApplicationContext = new WebApplicationContext(applicationContext.getConfig(), applicationContext);
        BeanFactory webBeanFactory = new BeanFactory(webApplicationContext);
        webApplicationContext.setControllerBeanFactory(webBeanFactory);
        webApplicationContext.postInit();

        return webApplicationContext;
    }
}
