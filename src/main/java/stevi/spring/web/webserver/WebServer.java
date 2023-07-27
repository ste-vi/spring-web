package stevi.spring.web.webserver;

public interface WebServer {

    int DEFAULT_PORT = 8080;

    int getPort();

    void start();

    void stop();
}
