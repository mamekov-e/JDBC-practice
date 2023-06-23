package org.example;

import org.example.config.HikariCPConnector;
import org.example.dao.UserDao;
import org.example.models.Role;
import org.example.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class JdbcAppTest {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        System.out.println("CONSOLE APP ---- TESTDB");
        System.out.println("Connection to test db");
        HikariCPConnector.getConnection();

        boolean exit = false;
        while (!exit) {
            System.out.println("Choose query (enter number):\n" +
                    " [1] insert user\n [2] update user\n [3] get user\n [4] get all users\n [5] delete user\n [6] quit");
            String userInput = input.next();

            exit = processUserInput(userInput);
        }
    }

    private static boolean processUserInput(String userInput) {
        int toNumber = 0;

        try {
            toNumber = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            System.out.println("Enter number!\n");
            return false;
        }

        UserDao userDao = new UserDao();
        boolean result;

        switch (toNumber) {
            case 1:
                System.out.print("\nFill data below: \n");
                User userToInsert = new User();

                System.out.print("\nEnter username: ");
                userToInsert.setUsername(input.next());
                System.out.print("\nEnter password: ");
                userToInsert.setPassword(input.next());
                userToInsert.setRole(Role.USER);

                result = userDao.insert(userToInsert);

                if (result)
                    System.out.print("\n-- User data added successfully --\n");
                else
                    System.out.println("-- User data was not added --\n");
                break;
            case 2:
                System.out.print("\nEnter username to update user data: ");
                String usernameToUpdate = input.next();
                User userToUpdate = userDao.select(usernameToUpdate);
                if (userToUpdate == null) {
                    System.out.printf("-- User not found with `username`: %s --\n", usernameToUpdate);
                    return false;
                }

                System.out.print("\nEnter username: ");
                String updatedUsername = input.next();
                if (!updatedUsername.isEmpty()) {
                    userToUpdate.setUsername(updatedUsername);
                }

                System.out.print("\nEnter password: ");
                String updatedPassword = input.next();
                if (!updatedPassword.isEmpty()) {
                    userToUpdate.setPassword(updatedPassword);
                }

                result = userDao.update(userToUpdate);

                if (result)
                    System.out.print("\n-- User data updated successfully --\n");
                else
                    System.out.println("-- User data was not updated --\n");
                break;
            case 3:
                System.out.print("\nEnter `username` to get user data: ");
                String usernameToGet = input.next();

                User userSelected = userDao.select(usernameToGet);
                if (userSelected == null) {
                    System.out.println("-- User not found with `username`: " + usernameToGet + " --\n");
                    return false;
                }
                System.out.println("| User selected |\n" + userSelected);
                break;
            case 4:
                List<User> usersSelected = userDao.selectAll();
                if (usersSelected == null) {
                    System.out.println("-- Users table is empty --\n");
                    return false;
                }
                System.out.println("| Users selected |");
                for (User u : usersSelected) {
                    System.out.println(u);
                }
                break;
            case 5:
                System.out.print("\nEnter `username` to delete user data: ");
                String usernameToRemove = input.next();

                result = userDao.delete(usernameToRemove);

                if (result)
                    System.out.print("\n-- User data deleted successfully --\n");
                else
                    System.out.println("-- User not found --\n");
                break;
            case 6:
                System.out.print("\n-- quiting... --\n");
                return true;
            default:
                System.out.println("Choose one of the options provided!\n");
                break;
        }

        return false;
    }

}
