package stevi.spring.test;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotation.Service;

@Slf4j
@Service
public class TestService {

    public TestResponse getTestResponse() {
        log.info("DI works!");
        return new TestResponse();
    }
}
