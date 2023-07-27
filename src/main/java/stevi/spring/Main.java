package stevi.spring;

import stevi.spring.web.annotations.SpringWebApplication;
import stevi.spring.web.context.WebApplication;
import stevi.spring.web.webserver.tomcat.TomcatWebServer;

@SpringWebApplication
public class Main {

    public static void main(String[] args) {
        WebApplication.run("stevi.spring", TomcatWebServer.class);
    }
}