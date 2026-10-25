package com.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {}

    public static List<HashMap<String, String>> parseJsonToListOfMaps(String resourcePath) {
        try (InputStream stream = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource file not found on classpath: " + resourcePath);
            }
            return MAPPER.readValue(stream, new TypeReference<List<HashMap<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON data from resource: " + resourcePath, e);
        }
    }
}