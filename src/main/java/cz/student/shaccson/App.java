package cz.student.shaccson;

import cz.common.DbUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class App {
    public static void main(String[] args) {
        try (Connection conn = DbUtil.getConnection()) {
            System.out.println("Connected to the database");
            UserDao userDAO = new UserDao(conn);
            // Test pridani uzivatele
            User newUser = new User();
            newUser.setEmail("testuser1@gmail.com");
            newUser.setUsername("testuser1");
            newUser.setPassword("password");
            userDAO.addUser(newUser);
            System.out.println("Added user with ID: " + newUser.getId());
            // Test retrieve uzivatele dle ID
            User retrievedUser = userDAO.getUserById(newUser.getId());
            if (retrievedUser != null) {
                System.out.println("Retrieved user: " + retrievedUser.getUsername());
            }
            // Test updatovani uzivatele
            newUser.setEmail("updateduser@example.com");
            newUser.setUsername("newtestuser1");
            newUser.setPassword("newpassword");
            userDAO.updateUser(newUser);
            System.out.println("Updated user with ID: " + newUser.getId());
            // Test listu vsech uzivatelu
            List<User> users = userDAO.getAllUsers();
            System.out.println("All users in database:");
            for (User user : users) {
                System.out.println("ID: " + user.getId() + ", Email: " + user.getEmail());
            }
            // Test smazani uzivatele
            userDAO.deleteUser(newUser.getId());
            System.out.println("Deleted user with ID: " + newUser.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}