package stevi.spring.test;

import lombok.extern.slf4j.Slf4j;
import stevi.spring.core.anotation.Autowired;
import stevi.spring.core.anotation.Value;
import stevi.spring.web.annotations.Controller;
import stevi.spring.web.annotations.DeleteMapping;
import stevi.spring.web.annotations.GetMapping;
import stevi.spring.web.annotations.PatchMapping;
import stevi.spring.web.annotations.PathVariable;
import stevi.spring.web.annotations.PostMapping;
import stevi.spring.web.annotations.PutMapping;
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

    @GetMapping("/{userId}/profile/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse getUserProfile(@PathVariable("profileId") Integer profileId, @PathVariable("userId") String userId) {
        log.info("getUserProfile");
        return testService.getTestResponse(userId, profileId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse searchUsersByNameAndAge(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        log.info("searchUsersByNameAndAge");
        return testService.getTestResponse(name, age);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TestResponse getAllUsers() {
        log.info("getAllUsers");
        return testService.getTestResponse("name", 31);
    }

    @PostMapping("/{userId}/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserProfile(@PathVariable("userId") String userId, @RequestBody TestRequest testRequest, @RequestParam("name") String name) {
        log.info("createUser");
        log.info(userId);
        log.info(name);
        log.info(testRequest.toString());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody TestRequest testRequest) {
        log.info("createUser");
        log.info(testRequest.toString());
    }

    @PutMapping("/{userId}/profile")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserProfile(@PathVariable("userId") String userId, @RequestBody TestRequest testRequest, @RequestParam("name") String name) {
        log.info("updateUserProfile");
        log.info(userId);
        log.info(name);
        log.info(testRequest.toString());
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable("userId") String userId) {
        log.info("updateUser");
        log.info(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void partialUpdate(@PathVariable("userId") String userId) {
        log.info("partialUpdate");
        log.info(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") String userId) {
        log.info("deleteUser");
        log.info(userId);
    }

    @DeleteMapping("/{userId}/profile/{profileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserProfile(@PathVariable("userId") String userId, @PathVariable("profileId") String profileId) {
        log.info("deleteUserProfile");
        log.info(userId);
        log.info(profileId);
    }

}
