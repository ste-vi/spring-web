package stevi.spring.web.context;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WebApplicationTest {

    @Test
    void test() {

        String requestPath = "/users/1/profiles/2/menu";
        String controllerPath = "/users/{id}/profiles/{profileId}/menu";

        boolean isMatch = isUrlMatchController(requestPath, controllerPath);

        assertTrue(isMatch);
    }

    private boolean isUrlMatchController(String requestPath, String controllerPath) {
        List<String> requestPathParts = Arrays.stream(requestPath.split("/")).toList();
        List<String> controllerPathParts = Arrays.stream(controllerPath.split("/")).toList();

        boolean isMatch = false;

        for (int i = 0; i < requestPathParts.size(); i++) {
            String requestString = requestPathParts.get(i);
            String controllerString = controllerPathParts.get(i);

            boolean stringsAreEqual = requestString.equals(controllerString);
            boolean numbersAreEqual = isRequestPathPartStringNumber(requestString) && isControllerPathPartStringExpectsIdParameter(controllerString);

            if (stringsAreEqual || numbersAreEqual) {
                isMatch = true;
            } else {
                isMatch = false;
                break;
            }
        }
        return isMatch;
    }

    private boolean isRequestPathPartStringNumber(String requestString) {
        try {
            return Integer.parseInt(requestString) > -1 || Long.parseLong(requestString) > -1;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private boolean isControllerPathPartStringExpectsIdParameter(String controllerString) {
        return controllerString.startsWith("{") && controllerString.endsWith("}");
    }

}