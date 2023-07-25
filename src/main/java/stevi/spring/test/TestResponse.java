package stevi.spring.test;

import lombok.Data;

@Data
public class TestResponse {

    private Long id = 1L;
    private String message = "it works!";
    private String name;
    private Integer age;
}
