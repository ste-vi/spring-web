package stevi.spring.web.webserver.unset;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.web.webserver.WebServerStarter;

@Slf4j
public class UnsetWebServerStarter implements WebServerStarter {

    @Override
    public void start() {
        log.error("Cannot start server. No web server set");
    }

    @Override
    public void stop() {
        log.error("Cannot start server. No web server set");
    }
}
