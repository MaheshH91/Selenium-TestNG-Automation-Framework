package rahulshettyacademy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<HashMap<String, String>> getJsonDataToMapList(String resourcePath) {
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource file not found on classpath: " + resourcePath);
            }
            return mapper.readValue(is, new TypeReference<List<HashMap<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON file: " + resourcePath, e);
        }
    }
}