package jdbc.dao;

import models.User;

import javax.sql.rowset.JdbcRowSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseDao<User, String> {

    @Override
    public List<User> selectAll() {

        try (JdbcRowSet jdbcRowSet = getJdbcRowSet()) {
            List<User> userList = new ArrayList<>();
            jdbcRowSet.beforeFirst();
            while (jdbcRowSet.next()) {
                int id = jdbcRowSet.getInt("id");
                String username = jdbcRowSet.getString("username");
                String password = jdbcRowSet.getString("password");
                String role_name = jdbcRowSet.getString("role_name");
                userList.add(new User(id, username, password, role_name));
            }
            userList.forEach(System.out::println);
            return userList;
        } catch (SQLException e) {
            System.out.println("Error when establishing connection in JdbcRowSet and executing SELECT USERs query: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(User user) {
        try (JdbcRowSet jdbcRs = getJdbcRowSet()) {
            jdbcRs.moveToInsertRow();

            jdbcRs.updateInt("id", user.getId());
            jdbcRs.updateString("username", user.getUsername());
            jdbcRs.updateString("password", user.getPassword());
            jdbcRs.updateString("role_name", user.getRole_name());

            jdbcRs.insertRow();
            return true;
        } catch (SQLException e) {
            System.out.println("Error when inserting data to users table:");
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(User user) {
        try(JdbcRowSet jdbcRowSet = getJdbcRowSet()) {
            while(jdbcRowSet.next()) {
                int userId = jdbcRowSet.getInt("id");
                if(userId == user.getId()) {
                    jdbcRowSet.updateString("username", user.getUsername());
                    jdbcRowSet.updateString("password", user.getPassword());
                    jdbcRowSet.updateRow();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error when updating users table:");
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(String username) {
        try (JdbcRowSet jdbcRowSet = getJdbcRowSet()) {
            while(jdbcRowSet.next()) {
                String usernameRs = jdbcRowSet.getString("username");
                if(usernameRs.equals(username)) {
                    jdbcRowSet.deleteRow();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error when deleting data from users table:");
            e.printStackTrace();
        }

        return false;
    }
}
