package org.example;

import org.example.config.HikariCPConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.utils.LoggerUtil.log;

public class JdbcAppTest {
    public static void main(String[] args) {
        String createCustomerTableQuery = "create table customer (id int not null primary key, name varchar(50) not null)";

        try (Connection connection = HikariCPConnector.getConnection();
             Statement statement = connection.createStatement()) {

            log("Creating customer table");
            statement.execute(createCustomerTableQuery);
        } catch (SQLException e) {
            log("Connection failed");
            e.printStackTrace();
        }
    }
}
