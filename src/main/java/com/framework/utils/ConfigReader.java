package com.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();
    private static final String DEFAULT_CONFIG = "config.properties";

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG)) {
            if (input == null) {
                throw new IllegalStateException("Classpath resource not found: " + DEFAULT_CONFIG);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to initialize configuration properties: " + e.getMessage());
        }
    }

    private ConfigReader() {}

    public static String getProperty(String key) {
        String cliValue = System.getProperty(key);
        if (cliValue != null && !cliValue.isBlank()) {
            return cliValue.trim();
        }
        String propertyValue = properties.getProperty(key);
        return propertyValue != null ? propertyValue.trim() : null;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return (value != null && !value.isBlank()) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}