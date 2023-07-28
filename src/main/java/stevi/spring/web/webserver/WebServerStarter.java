package stevi.spring.web.webserver;

/**
 * Facade for starting web server.
 */
public interface WebServerStarter {

    String SERVER_PORT_PROPERTY = "server.port";

    void start();

    void stop();
}
