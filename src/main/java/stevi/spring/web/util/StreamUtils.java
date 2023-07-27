package stevi.spring.web.util;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class StreamUtils {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    public static byte[] convertToByteArray(InputStream inputStream) {
        return toByteArray(inputStream, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] convertToByteArray(InputStream inputStream, int bufferSize) {
        bufferSize = bufferSize > 0 ? DEFAULT_BUFFER_SIZE : bufferSize;
        return toByteArray(inputStream, bufferSize);
    }

    @SneakyThrows
    private static byte[] toByteArray(InputStream inputStream, int bufferSize) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try (inputStream; byteOutput) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteOutput.write(buffer, 0, bytesRead);
            }
        }
        return byteOutput.toByteArray();
    }
}
