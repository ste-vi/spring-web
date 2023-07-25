package stevi.spring.test;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotation.Service;

@Slf4j
@Service
public class TestService {

    public TestResponse getTestResponse(String name, Integer age) {
        log.info("DI works!");
        TestResponse testResponse = new TestResponse();
        testResponse.setName(name);
        testResponse.setAge(age);

        return testResponse;
    }
}
