import jdbc.SQLQueries;
import jdbc.dao.UserDao;
import models.User;
import utils.JdbcUtil;

import javax.sql.rowset.*;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTest {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        JdbcUtil.setPropData();
        connection = JdbcUtil.getConnection();

        // JdbcRowSet example
//        testJdbcRowSet();

        // CachedRowSet example
//        testCachedRowSet();

        // JoinRowSet example
//        testJoinRowSet();

        // WebRowSet example
        testWebRowSet();
    }

    private static void testJdbcRowSet() {
        UserDao userDao = new UserDao();

        // select all rows
        System.out.println("SELECT ALL:");
        userDao.selectAll();

        // insert data
        System.out.println("INSERT DATA: User(id=X, username=u5, password=p5, role_name=USER)");
        userDao.insert(new User(7, "u5", "p5", "USER"));
        // check inserted row
        userDao.selectAll();

        // update data
        System.out.println("UPDATE DATA: User(id=X, username=u7, password=p7, role_name=USER)");
        userDao.update(new User(7, "u7", "p7", "USER"));
        // check updated row
        userDao.selectAll();

        // delete data
        System.out.println("DELETE DATA: User(id=X, username=u7, password=p7, role_name=USER)");
        userDao.delete("u7");
        // check deleted row
        userDao.selectAll();
    }

    private static void testCachedRowSet() throws SQLException {
        RowSetFactory rowSetFactory = RowSetProvider.newFactory();

        try (CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet()) {
            cachedRowSet.setUrl(JdbcUtil.url);
            cachedRowSet.setUsername(JdbcUtil.user);
            cachedRowSet.setPassword(JdbcUtil.password);
            connection.setAutoCommit(false);

            cachedRowSet.setCommand(SQLQueries.selectAll("users"));
            cachedRowSet.execute();
            // setting pk for a set
            int[] pkKeys = {1};
            cachedRowSet.setKeyColumns(pkKeys);

            // output users table rows
            while (cachedRowSet.next()) {
                int id = cachedRowSet.getInt("id");
                String username = cachedRowSet.getString("username");
                String password = cachedRowSet.getString("password");
                String role_name = cachedRowSet.getString("role_name");
                System.out.println(new User(id, username, password, role_name));
            }

            // update username = u9 for user with id 9
            cachedRowSet.last();
            cachedRowSet.updateString("username", "u9");
            cachedRowSet.updateString("password", "p9");
            // updated only CachedRowSet object data
            cachedRowSet.updateRow();

            try {
                // submit changes also to dbms
                cachedRowSet.acceptChanges(connection);
            } catch (SyncProviderException syncProviderException) {
                SyncResolver syncResolver = syncProviderException.getSyncResolver();

                Object cachedRowSetValue;
                Object resolverValue;
                Object resolvedValue;

                while (syncResolver.nextConflict()) {
                    if (syncResolver.getStatus() == SyncResolver.UPDATE_ROW_CONFLICT) {
                        int conflictRow = syncResolver.getRow();
                        cachedRowSet.absolute(conflictRow);

                        int columnsNumOfConflictRow = syncResolver.getMetaData().getColumnCount();
                        for (int conflictColumnIndex = 1; conflictColumnIndex <= columnsNumOfConflictRow; conflictColumnIndex++) {
                            if (syncResolver.getConflictValue(conflictColumnIndex) != null) {
                                cachedRowSetValue = cachedRowSet.getObject(conflictColumnIndex);
                                resolverValue = syncResolver.getConflictValue(conflictColumnIndex);

                                // here we choose which value to use for resolving the conflict
                                resolvedValue = cachedRowSetValue;
                                syncResolver.setResolvedValue(conflictColumnIndex, resolvedValue);
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static void testJoinRowSet() throws SQLException {
        RowSetFactory rowSetFactory = RowSetProvider.newFactory();

        try (CachedRowSet usersRowSet = rowSetFactory.createCachedRowSet();
             CachedRowSet productsRowSet = rowSetFactory.createCachedRowSet();
             JoinRowSet joinRowSet = rowSetFactory.createJoinRowSet()) {

            usersRowSet.setUrl(JdbcUtil.url);
            usersRowSet.setUsername(JdbcUtil.user);
            usersRowSet.setPassword(JdbcUtil.password);
            usersRowSet.setCommand(SQLQueries.selectAll("users"));
            usersRowSet.execute();

            productsRowSet.setUrl(JdbcUtil.url);
            productsRowSet.setUsername(JdbcUtil.user);
            productsRowSet.setPassword(JdbcUtil.password);
            productsRowSet.setCommand(SQLQueries.selectAll("products"));
            productsRowSet.execute();

            joinRowSet.addRowSet(usersRowSet, "id");
            joinRowSet.addRowSet(productsRowSet, "user_id");

            while (joinRowSet.next()) {
                System.out.println(joinRowSet.getString("username"));
                System.out.println(joinRowSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testWebRowSet() throws SQLException {
        RowSetFactory rowSetFactory = RowSetProvider.newFactory();

        try (WebRowSet webRowSet = rowSetFactory.createWebRowSet();
             FileWriter fileWriter = new FileWriter("usersList.xml");
             FileReader fileReader = new FileReader("usersList.xml")) {
            webRowSet.setUrl(JdbcUtil.url);
            webRowSet.setUsername(JdbcUtil.user);
            webRowSet.setPassword(JdbcUtil.password);
            connection.setAutoCommit(false);

            webRowSet.setCommand(SQLQueries.selectAll("users"));
            int[] keys = {1};
            webRowSet.setKeyColumns(keys);
            webRowSet.execute();

            webRowSet.moveToInsertRow();

            webRowSet.updateInt("id", 10);
            webRowSet.updateString("username", "u10");
            webRowSet.updateString("password", "p10");
            webRowSet.updateString("role_name", "USER");
            webRowSet.insertRow();
            webRowSet.moveToCurrentRow();

            webRowSet.absolute(2);
//            webRowSet.deleteRow();

            webRowSet.writeXml(fileWriter);

            webRowSet.acceptChanges(connection);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
