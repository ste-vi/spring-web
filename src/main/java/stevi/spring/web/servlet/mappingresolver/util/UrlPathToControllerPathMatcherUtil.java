package stevi.spring.web.servlet.mappingresolver.util;

import java.util.Arrays;
import java.util.List;

/**
 * Util class that helps match incoming url request path with controller handler method path.
 */
public final class UrlPathToControllerPathMatcherUtil {

    /**
     * Checks if request path is matched to controller method full path.
     */
    public static boolean isUrlMatchController(String requestPath, String controllerMethodFullPath) {
        List<String> requestPathParts = Arrays.stream(requestPath.split("/")).toList();
        List<String> methodPathParts = Arrays.stream(controllerMethodFullPath.split("/")).toList();

        if (requestPathParts.size() != methodPathParts.size()) {
            return false;
        }

        boolean isMatch = false;
        for (int i = 0; i < requestPathParts.size(); i++) {
            String requestString = requestPathParts.get(i);
            String controllerString = methodPathParts.get(i);

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

    private static boolean isRequestPathPartStringNumber(String requestString) {
        try {
            return Integer.parseInt(requestString) > -1 || Long.parseLong(requestString) > -1;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isControllerPathPartStringExpectsIdParameter(String controllerString) {
        return controllerString.startsWith("{") && controllerString.endsWith("}");
    }
}
