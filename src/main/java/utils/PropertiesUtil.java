package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties;

    private PropertiesUtil() {
    }

    public static Properties getInstance() {
        if (properties == null) {
            initProperties();
        }
        return properties;
    }

    private static void initProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readProperty(String keyName) {
        return getInstance().getProperty(keyName);
    }
}
