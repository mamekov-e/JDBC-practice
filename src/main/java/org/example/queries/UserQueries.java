package org.example.queries;

public enum UserQueries {
    GET("SELECT * FROM users WHERE username = ?;"),
    GET_ALL("SELECT * FROM users;"),
    INSERT("INSERT INTO users (username, password, role_name) VALUES (?, ?, ?) RETURNING id;"),
    DELETE("DELETE FROM users WHERE username = ? RETURNING id;"),
    UPDATE("UPDATE users SET username = ?, password = ? WHERE id = ? RETURNING id;");

    public String QUERY;

    UserQueries(String QUERY) {
        this.QUERY = QUERY;
    }
}
