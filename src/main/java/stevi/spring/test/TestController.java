package stevi.spring.test;

import stevi.spring.core.anotation.Autowired;
import stevi.spring.web.annotations.Controller;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.RequestParam;
import stevi.spring.web.annotations.ResponseStatus;
import stevi.spring.web.http.HttpStatus;

@Controller
@RequestMapping("/users")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/{userId}/profile/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse get(@PathVariable("userid") Long userId,
                            @PathVariable("profileId") String profileId,
                            @RequestParam("name") String name,
                            @RequestParam("age") Integer age) {
        return testService.getTestResponse();
    }
}
