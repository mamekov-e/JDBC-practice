import utils.JdbcUtil;

import java.sql.*;

public class JdbcTest {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        JdbcUtil.setPropData();
        connection = JdbcUtil.getConnection();

        create_procedure_show_user_by_username();

        CallableStatement callableStatement = connection.prepareCall("{call show_user_by_username(?, ?)}");
        callableStatement.setString(1, "u10");
        callableStatement.registerOutParameter(2, Types.VARCHAR);
        ResultSet rs = callableStatement.executeQuery();
        if (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            System.out.println(username + " : " + password);
        }
    }

    private static void create_procedure_show_user_by_username() {
        String drop_show_user_by_username_procedure_sql =
                "drop procedure if exists show_user_by_username()";

        String create_show_user_by_username_sql =
                "create or replace procedure show_user_by_username(a inout varchar) " +
                        "as " +
                        "'begin " +
                        "    select * from users where username = a; " +
                        "end;' language plpgsql";

        try (Statement statement = connection.createStatement()) {
            System.out.println("dropping procedure show_user_by_username");
            statement.execute(drop_show_user_by_username_procedure_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            System.out.println("creating procedure show_user_by_username");
            statement.executeUpdate(create_show_user_by_username_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
