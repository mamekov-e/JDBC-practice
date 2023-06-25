package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
    public static String driver;
    public static String url;
    public static String user;
    public static String password;

    public static void setPropData() {
        driver = PropertiesUtil.readProperty("driver");
        url = PropertiesUtil.readProperty("url");
        user = PropertiesUtil.readProperty("user");
        password = PropertiesUtil.readProperty("password");
    }

    public static Connection getConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
