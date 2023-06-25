import utils.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTest {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        JdbcUtil.setPropData();
        connection = JdbcUtil.getConnection();

    }

}
