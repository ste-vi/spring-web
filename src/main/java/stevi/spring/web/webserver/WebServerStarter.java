package stevi.spring.web.webserver;

public interface WebServerStarter {

    String SERVER_PORT_PROPERTY = "server.port";

    void start();

    void stop();
}
