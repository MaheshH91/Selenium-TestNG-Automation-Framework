package rahulshettyacademy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static final String DEFAULT_CONFIG_FILE = "config.properties";

    static {
        loadProperties(DEFAULT_CONFIG_FILE);
    }

    private static void loadProperties(String propertyFileName) {
        properties = new Properties();
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            if (is == null) {
                throw new RuntimeException("Property file not found on classpath: " + propertyFileName);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read property file: " + propertyFileName, e);
        }
    }

    public static String getProperty(String key) {
        // System property (from Maven CLI -Dkey=value) takes precedence over config file
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }
        String fileValue = properties.getProperty(key);
        return fileValue != null ? fileValue.trim() : null;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}