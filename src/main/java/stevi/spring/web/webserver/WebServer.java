package stevi.spring.web.webserver;

/**
 * Interface represents contract for any web server implementation.
 */
public interface WebServer {

    int DEFAULT_PORT = 8080;

    int getPort();

    void start();

    void stop();
}
