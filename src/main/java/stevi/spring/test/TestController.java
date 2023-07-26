package stevi.spring.test;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotation.Autowired;
import stevi.spring.web.annotations.Controller;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.PostMapping;
import stevi.spring.web.annotations.RequestBody;
import stevi.spring.web.annotations.RequestMapping;
import stevi.spring.web.annotations.RequestParam;
import stevi.spring.web.annotations.ResponseStatus;
import stevi.spring.web.http.HttpStatus;

@Slf4j
@Controller
@RequestMapping("/users")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/{userId}/profile/{profileId}/menu")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse get(@PathVariable("profileId") Integer profileId, @PathVariable("userId") String userId) {
        return testService.getTestResponse(userId, profileId);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse get(@RequestParam("name") String name,
                            @RequestParam("age") Integer age,
                            @RequestParam("num") Integer num) {
        return testService.getTestResponse(name, age);
    }

    @GetMapping("/user2")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse getTwo(@RequestParam("name") String name,
                               @RequestParam("age") Integer age) {
        return testService.getTestResponse(name, age);
    }

    @PostMapping("/{userId}/post")
    @ResponseStatus(HttpStatus.OK)
    public void post(@PathVariable("userId") String userId, @RequestBody TestRequest testRequest, @RequestParam("name") String name) {
        log.info(userId);
        log.info(name);
        log.info(testRequest.toString());
    }
}
