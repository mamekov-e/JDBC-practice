package org.example.dao;

import org.example.config.HikariCPConnector;
import org.example.models.Role;
import org.example.models.User;
import org.example.queries.UserQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.LoggerUtil.log;

public class UserDao implements DaoBase<User, String> {
    @Override
    public User select(String usernameKey) {
        final User user = new User();
        user.setId(-1);

        try (Connection conn = HikariCPConnector.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UserQueries.GET.QUERY)) {
            preparedStatement.setString(1, usernameKey);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                Role role = Role.valueOf(rs.getString("role_name"));

                return new User(userId, username, password, role);
            }

        } catch (SQLException e) {
            log("Error when getting user: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> selectAll() {
        List<User> users = new ArrayList<>();

        try (Connection conn = HikariCPConnector.getConnection();
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery(UserQueries.GET_ALL.QUERY);
            while (rs.next()) {
                Integer userId = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                Role role = Role.valueOf(rs.getString("role_name"));
                users.add(new User(userId, username, password, role));
            }
            return users;
        } catch (SQLException e) {
            log("Error when selecting all users: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(User user) {
        boolean result = false;

        try (Connection conn = HikariCPConnector.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UserQueries.INSERT.QUERY)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().name());
            result = preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            log("Error when inserting user: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean delete(String username) {
        boolean result = false;

        try (Connection conn = HikariCPConnector.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UserQueries.DELETE.QUERY)) {
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            log("Error when deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(User user) {
        boolean result = false;
        try (Connection conn = HikariCPConnector.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UserQueries.UPDATE.QUERY)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getId());
            result = preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            log("Error when updating user: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
