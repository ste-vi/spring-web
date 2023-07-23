package stevi.spring.web.http;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestPath {

    private String fullPath;


}
