package stevi.spring.test;

import stevi.spring.web.annotations.Controller;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.ResponseStatus;

@Controller
@RequestMapping("")
public class TestController {

    @GetMapping
    @ResponseStatus(200)
    public TestResponse get() {
        return new TestResponse();
    }
}
