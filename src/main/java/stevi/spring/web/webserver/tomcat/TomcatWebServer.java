package stevi.spring.web.webserver.tomcat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import stevi.spring.web.servlet.DispatcherServlet;
import stevi.spring.web.webserver.WebServer;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Tomcat implementation of web server.
 */
@Slf4j
public class TomcatWebServer implements WebServer {

    private final int port;
    private final DispatcherServlet dispatcherServlet;
    private Tomcat tomcat;

    public TomcatWebServer(String port, DispatcherServlet dispatcherServlet, Class<?> mainClass) {
        this.port = port == null ? DEFAULT_PORT : Integer.parseInt(port);
        this.dispatcherServlet = dispatcherServlet;
        initTomcat(mainClass);
    }

    private void initTomcat(Class<?> mainClass) {
        disableDefaultLogging();
        tomcat = createTomcatServer();
        Context context = createContext(tomcat, mainClass);
        registerDispatcherServlet(context);
    }

    private void disableDefaultLogging() {
        Logger.getLogger("org.apache").setLevel(java.util.logging.Level.WARNING);
    }

    private Tomcat createTomcatServer() {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("/");
        tomcat.setPort(port);

        return tomcat;
    }

    private Context createContext(Tomcat tomcat, Class<?> mainClass) {
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext("", docBase);
        context.addLifecycleListener(new ContextConfig());

        final WebResourceRoot root = new StandardRoot(context);
        final URL url = findClassLocation(mainClass);
        root.createWebResourceSet(WebResourceRoot.ResourceSetType.PRE, "/WEB-INF/classes", url, "/");

        context.setResources(root);

        return context;
    }

    private URL findClassLocation(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    private void registerDispatcherServlet(Context context) {
        Tomcat.addServlet(context, "DispatcherServlet", dispatcherServlet);
        context.addServletMappingDecoded("/*", "DispatcherServlet");
    }

    @Override
    public int getPort() {
        return port;
    }

    @SneakyThrows
    @Override
    public void start() {
        log.info("Tomcat started on port {}", port);
        startConnection(tomcat);
    }

    private void startConnection(Tomcat tomcat) throws LifecycleException {
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    @SneakyThrows
    @Override
    public void stop() {
        tomcat.stop();
    }
}
