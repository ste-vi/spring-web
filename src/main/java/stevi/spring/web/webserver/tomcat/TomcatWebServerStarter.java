package stevi.spring.web.webserver.tomcat;

import org.reflections.Reflections;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.env.Environment;
import stevi.spring.web.annotations.SpringWebApplication;
import stevi.spring.web.context.WebApplicationContext;
import stevi.spring.web.servlet.DispatcherServlet;
import stevi.spring.web.webserver.WebServerStarter;

import java.util.Set;

/**
 * Starter class for tomcat web server.
 */
public class TomcatWebServerStarter implements WebServerStarter {

    private final TomcatWebServer webServer;

    public TomcatWebServerStarter(WebApplicationContext webApplicationContext) {
        webServer = initWebServer(webApplicationContext);
    }

    private TomcatWebServer initWebServer(WebApplicationContext webApplicationContext) {
        Environment environment = webApplicationContext.getBean(Environment.class);
        String port = environment.getProperty(SERVER_PORT_PROPERTY);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);

        Class<?> mainClass = resolveMainClass(webApplicationContext);

        return new TomcatWebServer(port, dispatcherServlet, mainClass);
    }

    private static Class<?> resolveMainClass(ApplicationContext applicationContext) {
        Reflections reflectionsScanner = applicationContext.getConfig().getReflectionsScanner();
        Set<Class<?>> mainClasses = reflectionsScanner.getTypesAnnotatedWith(SpringWebApplication.class);
        if (mainClasses.size() > 1) {
            throw new RuntimeException("Cannot resolve Main class, more then one %s annotation usage found".formatted(SpringWebApplication.class.getName()));
        } else if (mainClasses.isEmpty()) {
            throw new RuntimeException("Cannot resolve Main class, please mark Main class with %s annotation".formatted(SpringWebApplication.class.getName()));
        }
        return mainClasses.iterator().next();
    }

    @Override
    public void start() {
        webServer.start();
    }

    @Override
    public void stop() {
        webServer.stop();
    }
}
