package stevi.spring;

import stevi.spring.web.TomcatStarter;

public class Main {

    public static void main(String[] args) {
        TomcatStarter tomcatStarter = new TomcatStarter();
        tomcatStarter.start();
    }
}