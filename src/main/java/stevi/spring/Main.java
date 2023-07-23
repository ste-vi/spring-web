package stevi.spring;

import stevi.spring.core.context.ApplicationContext;
import stevi.spring.web.context.WebApplication;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = WebApplication.run("stevi.spring");
    }
}