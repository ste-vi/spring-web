package stevi.spring.test;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotation.Service;
import stevi.spring.core.anotation.Value;

@Slf4j
@Service
public class TestService {

    @Value
    private String test;

    public TestResponse getTestResponse(String name, Integer age) {

        log.info(test);

        log.info("DI works!");
        TestResponse testResponse = new TestResponse();
        testResponse.setName(name);
        testResponse.setAge(age);

        return testResponse;
    }

}
