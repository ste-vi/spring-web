package stevi.spring.web;

import lombok.SneakyThrows;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import stevi.spring.Main;

import java.io.File;
import java.net.URL;

public class TomcatStarter {

    @SneakyThrows
    public void start() {
        Tomcat tomcat = createTomcatServer();
        configureContext(tomcat);
        setupConnection(tomcat);
    }

    private Tomcat createTomcatServer() {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("/");
        tomcat.setPort(8080);

        return tomcat;
    }

    private void configureContext(Tomcat tomcat) {
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext("", docBase);
        context.addLifecycleListener(new ContextConfig());

        final WebResourceRoot root = new StandardRoot(context);
        final URL url = findClassLocation(Main.class);
        root.createWebResourceSet(WebResourceRoot.ResourceSetType.PRE, "/WEB-INF/classes", url, "/");

        context.setResources(root);
    }

    private void setupConnection(Tomcat tomcat) throws LifecycleException {
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    private URL findClassLocation(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }
}
