package stevi.spring.web.context;

import org.junit.jupiter.api.Test;
import stevi.spring.core.context.Application;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.web.webserver.tomcat.TomcatWebServer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebApplicationTest {

    @Test
    void testWebApplication() {
        ApplicationContext applicationContext = WebApplication.run("stevi.spring", TomcatWebServer.class);
        WebApplication.stop();
        assertNotNull(applicationContext);
    }

    @Test
    void testApplication() {
        ApplicationContext applicationContext = Application.run("stevi.spring");
        assertNotNull(applicationContext);
    }
}