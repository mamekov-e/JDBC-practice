package jdbc;

public class SQLQueries {
    private static final String SELECT_Q = "SELECT * FROM %s";

    public static String selectAll(String table) {
        return String.format(SELECT_Q, table);
    }
}
