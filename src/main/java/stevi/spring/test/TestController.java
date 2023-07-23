package stevi.spring.test;

import stevi.spring.core.anotation.Autowired;
import stevi.spring.web.annotations.Controller;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.ResponseStatus;
import stevi.spring.web.http.HttpStatus;

@Controller
@RequestMapping("")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TestResponse get() {
        return testService.getTestResponse();
    }
}
