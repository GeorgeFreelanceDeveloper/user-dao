package cz.student.shaccson;

import cz.common.DbUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("Connected to the database");
            final UserDao userDAO = new UserDao();
            // Test pridani uzivatele
            final User newUser = new User();
            newUser.setEmail("testuser1@gmail.com");
            newUser.setUsername("testuser1");
            newUser.setPassword("password");
            userDAO.addUser(newUser);
            System.out.println("Added user with ID: " + newUser.getId());
            // Test retrieve uzivatele dle ID
            final User retrievedUser = userDAO.getUserById(newUser.getId());
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
            final List<User> users = userDAO.getAllUsers();
            System.out.println("All users in database:");
            for (User user : users) {
                System.out.println("ID: " + user.getId() + ", Email: " + user.getEmail());
            }
            // Test smazani uzivatele
            userDAO.deleteUser(newUser.getId());
            System.out.println("Deleted user with ID: " + newUser.getId());

        } catch (Exception e) {
            System.err.println("An unexpected error occurred in the application: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}