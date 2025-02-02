package cz.student.simon8491;

import java.util.List;
import java.util.Scanner;

public class AppMenu {
    static final String[] MENU = {"CREATE","UPDATE","READ_ONE","READ_ALL","DELETE","EXIT"};
    private static final Scanner userInput = new Scanner(System.in);

    public static void displayMenu() {
        System.out.println("\nSelect the operation you would like to perform:\n");
        for (String option : MENU) {
            System.out.println(option);
        }
    }

    public static void operationSelect() {
        do {
            switch (userInput.nextLine().toUpperCase()) {
                case "CREATE" -> createUser();
                case "UPDATE" -> updateUser();
                case "READ_ONE" -> readOneUser();
                case "READ_ALL" -> readAllUsers();
                case "DELETE" -> deleteUser();
                case "EXIT" -> {
                    userInput.close();
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option");
            }
            displayMenu();
        } while (userInput.hasNext());
        userInput.close();
    }
    public static void createUser() {
        UserDao userDao = new UserDao();
        User user = new User();
        System.out.print("Enter username: ");
        user.setUserName(userInput.nextLine());
        System.out.print("Enter email address: ");
        user.setEmail(userInput.nextLine());
        System.out.print("Enter your password: ");
        user.setPassword(userInput.nextLine());
        userDao.createUser(user);
    }

    public static void updateUser() {
        UserDao userDao = new UserDao();
        System.out.print("Enter user ID: ");
        User userUpdate = userDao.readUser(userInput.nextInt());
        userInput.nextLine();
        System.out.print("Enter new username: ");
        userUpdate.setUserName(userInput.nextLine());
        System.out.print("Enter new email address: ");
        userUpdate.setEmail(userInput.nextLine());
        System.out.print("Enter new password: ");
        userUpdate.setPassword(userInput.nextLine());
        userDao.updateUser(userUpdate);
    }

    public static void readOneUser() {
        UserDao userDao = new UserDao();
        System.out.print("Enter user ID: ");
        User userToRead = userDao.readUser(userInput.nextInt());
        if (userToRead == null) {
            System.out.println("User not found");
        } else {
            System.out.print(userToRead);
        }
    }

    public static void readAllUsers() {
        UserDao userDao = new UserDao();
        List<User> users = userDao.findAll();
        for (User oneUser : users) {
            System.out.println(oneUser + "\n");
        }
    }

    public static void deleteUser() {
        UserDao userDao = new UserDao();
        System.out.print("Enter user ID to be deleted: ");
        userDao.deleteUser(userInput.nextInt());
    }
}
