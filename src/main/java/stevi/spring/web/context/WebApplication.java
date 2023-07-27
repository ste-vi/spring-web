package stevi.spring.web.context;

import stevi.spring.core.context.Application;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.factory.BeanFactory;
import stevi.spring.web.webserver.WebServer;
import stevi.spring.web.webserver.WebServerStarter;
import stevi.spring.web.webserver.tomcat.TomcatWebServer;
import stevi.spring.web.webserver.tomcat.TomcatWebServerStarter;
import stevi.spring.web.webserver.unset.UnsetWebServerStarter;

public class WebApplication {

    private static WebServerStarter webServerStarter;

    public static ApplicationContext run(String packagesToScan, Class<? extends WebServer> webServerClass) {
        ApplicationContext applicationContext = Application.run(packagesToScan);
        WebApplicationContext webApplicationContext = createWebApplicationContext(applicationContext);
        runWebServer(webServerClass, webApplicationContext);

        return webApplicationContext;
    }

    private static WebApplicationContext createWebApplicationContext(ApplicationContext applicationContext) {
        WebApplicationContext webApplicationContext = new WebApplicationContext(applicationContext.getConfig(), applicationContext);
        BeanFactory webBeanFactory = new BeanFactory(webApplicationContext);
        webApplicationContext.setWebBeanFactory(webBeanFactory);
        webApplicationContext.postInit();

        return webApplicationContext;
    }

    private static void runWebServer(Class<? extends WebServer> webServerClass, WebApplicationContext webApplicationContext) {
        webServerStarter = applyWebServerStrategy(webServerClass, webApplicationContext);
        webServerStarter.start();
    }

    private static WebServerStarter applyWebServerStrategy(Class<? extends WebServer> webServerClass,
                                                           WebApplicationContext webApplicationContext) {
        if (webServerClass == TomcatWebServer.class) {
            return new TomcatWebServerStarter(webApplicationContext);
        } else {
            return new UnsetWebServerStarter();
        }
    }

    public static void stop() {
        Application.stop();
        webServerStarter.stop();
    }
}
