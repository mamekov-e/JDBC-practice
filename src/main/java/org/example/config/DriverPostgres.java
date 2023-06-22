package org.example.config;

import org.postgresql.PGConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverPostgres extends DriverBase {

    private final String CREATE_SCHEMA = "CREATE SCHEMA \"%s\"";
    private final String DROP_SCHEMA = "DROP SCHEMA \"%s\"";
    private PGConnection connection = null;

    private DriverPostgres() {
        super("org.postgresql.Driver");
    }

    @Override
    public void setUrl(String host, String database, String port) {
        if (database.length() > 0) {
            this.url = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
        } else {
            this.url = String.format("jdbc:postgresql://%s:%s", host, port);
        }
    }

    @Override
    public Connection getConnection() {
        return (Connection) connection;
    }

    @Override
    public void connect(String login, String password) {
        super.connect(login, password);

        try {
            connection = (PGConnection) DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createSchema(String schema) {
        return executeQuery(String.format(CREATE_SCHEMA, schema));
    }

    @Override
    public boolean dropSchema(String schema) {
        return executeQuery(String.format(DROP_SCHEMA, schema));
    }
}
