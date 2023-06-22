package org.example.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public abstract class DriverBase {
    protected String driver;
    protected String url;
    protected Properties properties = null;

    public DriverBase(String driver) {
        this.driver = driver;
    }

    protected void registerDriver() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void setUrl(String host, String database, String port);

    public abstract Connection getConnection();

    public void connect(String login, String password) {
        registerDriver();

        properties = new Properties();
        properties.setProperty("user", login);
        properties.setProperty("password", password);
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "utf8");
    }

    public void disconnect(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createSchema(String schema) {
        return false;
    }

    public boolean dropSchema(String schema) {
        return false;
    }

    public boolean executeQuery(final String query) {
        boolean result = false;

        try (Connection connection = getConnection()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                statement.execute(query);
                result = true;
            }
        } catch (SQLException e) {
            System.err.println("SQLException : code = " + e.getErrorCode() +
                    " - " + e.getMessage());
            System.err.println("\tSQL : " + query);
        }
        return result;
    }
}
