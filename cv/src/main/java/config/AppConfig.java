package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new RuntimeException("Konfigurační soubor config.properties nebyl nalezen!");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Chyba při načítání config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        String val = properties.getProperty(key);
        return (val != null) ? Integer.parseInt(val) : defaultValue;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String val = properties.getProperty(key);
        return (val != null) ? Boolean.parseBoolean(val) : defaultValue;
    }
}