package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static org.example.utils.LoggerUtil.log;

public class ConfPropertiesUtil {
    private final static String CONF_PROPERTIES_FILENAME = "application.properties";
    private static Properties properties;

    public static Properties getProperties() {
        if (properties != null) {
            return properties;
        }

        try (InputStream is = ConfPropertiesUtil.class.getClassLoader().getResourceAsStream(CONF_PROPERTIES_FILENAME)) {
            Properties newProp = new Properties();
            newProp.load(is);
            properties = newProp;
            return properties;
        } catch (IOException e) {
            log(e.getMessage());
            return null;
        }
    }

    public static String getValue(String key) {
        return Objects.requireNonNull(getProperties()).get(key).toString();
    }
}
