package stevi.spring.web.util;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class StreamUtils {

    @SneakyThrows
    public static byte[] convertToByteArray(InputStream inputStream) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

        try (inputStream; byteOutput) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteOutput.write(buffer, 0, bytesRead);
            }
        }

        return byteOutput.toByteArray();
    }
}
