package stevi.spring;

import stevi.spring.web.webserver.TomcatWebServer;

public class Main {

    public static void main(String[] args) {
        TomcatWebServer tomcatStarter = new TomcatWebServer();
        tomcatStarter.start();
    }
}