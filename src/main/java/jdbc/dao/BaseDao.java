package jdbc.dao;

import jdbc.SQLQueries;
import utils.JdbcUtil;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDao<Entity, Key> {
    abstract List<Entity> selectAll();

    abstract boolean insert(Entity entity);

    abstract boolean update(Entity entity);

    abstract boolean delete(Key key);

    public JdbcRowSet getJdbcRowSet() {
        try {
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();

            JdbcRowSet jdbcRowSet = rowSetFactory.createJdbcRowSet();
            jdbcRowSet.setUrl(JdbcUtil.url);
            jdbcRowSet.setUsername(JdbcUtil.user);
            jdbcRowSet.setPassword(JdbcUtil.password);

            jdbcRowSet.setCommand(SQLQueries.selectAll("users"));
            jdbcRowSet.execute();

            return jdbcRowSet;
        } catch (SQLException e) {
            System.out.println("Error when creating new row factory: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
