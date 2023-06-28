import jdbc.SQLQueries;
import utils.JdbcUtil;

import java.sql.*;

public class JdbcTest {
    private static Connection connection;

    public static void main(String[] args) {
        JdbcUtil.setPropData();
        connection = JdbcUtil.getConnection();

        create_procedure_transfer();

        try (PreparedStatement transferStmt =
                     connection.prepareStatement("call transfer(?, ?, ?)");
            PreparedStatement getAllAccountsDataStmt =
                    connection.prepareStatement(SQLQueries.selectAll("accounts"))
        ) {
            String sender = "Serik";
            String receiver = "Berik";
            double amount = 150;
            System.out.printf("Executing transfer procedure: transfer(sender:%s,receiver:%s,amount:%s)", sender, receiver, amount);
            transferStmt.setString(1, sender);
            transferStmt.setString(2, receiver);
            transferStmt.setDouble(3, amount);
            boolean executedOk = transferStmt.execute();
            System.out.println(executedOk);
            if (executedOk) {
                System.out.printf("Procedure executed successfully");
            }

            System.out.println("Checking balances changed:");
            ResultSet rs = getAllAccountsDataStmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("name");
                String balance = rs.getString("balance");
                System.out.println(username + " : " + balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void create_procedure_transfer() {
        String drop_transfer_procedure_sql =
                "drop procedure if exists transfer_jdbc";

        String create_transfer_sql =
                "create or replace procedure transfer_jdbc(" +
                        "sender accounts.name%type, " +
                        "receiver accounts.name%type," +
                        "amount accounts.balance%type) " +
                        "language plpgsql as $$ " +
                        "declare senderBalance accounts.balance%type := get_balance_of(sender); " +
                        "begin " +
                        "   if senderBalance >= amount then " +
                        "       update accounts set balance=balance-amount where name=sender; " +
                        "       update accounts set balance=balance+amount where name=receiver; " +
                        "    else rollback;" +
                        "   end if;" +
                        "   commit;" +
                        "end; " +
                        "$$";

        try (Statement statement = connection.createStatement()) {
            System.out.println("dropping procedure transfer");
            statement.execute(drop_transfer_procedure_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            System.out.println("creating procedure transfer");
            statement.executeUpdate(create_transfer_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
