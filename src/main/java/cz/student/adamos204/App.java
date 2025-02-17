package cz.student.adamos204;

import java.util.Scanner;
import java.util.function.Predicate;

public class App {

    private static final UserDao userDao = new UserDao();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] allowedCommands = {"create", "update", "delete", "finduser", "selectall", "info", "exit"};

    public static void main(String[] args) {

        System.out.println("System for user control on-line");
        displayAllowedCommands(allowedCommands);
        while (true) {
            System.out.println("Waiting for next command");
            final String command = scanner.nextLine();
            switch (command) {
                case "create" -> createUser();
                case "update" -> updateUser();
                case "delete" -> deleteUser();
                case "finduser" -> findUser();
                case "selectall" -> selectAllUsers();
                case "info" -> displayAllowedCommands(allowedCommands);
                case "exit" -> exit();
                default -> System.out.println("Unknown command");
            }
        }
    }

    private static void exit() {
        System.out.println("Exiting program");
        scanner.close();
        System.exit(0);
    }

    private static void selectAllUsers() {
        System.out.println("All users:");
        userDao.readAll();
    }

    private static void findUser() {
        while (true) {
            try {
                System.out.println("Enter user id to search database");
                final int userId = Integer.parseInt(scanner.nextLine());
                System.out.println(userDao.read(userId));
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter valid id");
            }
        }
    }

    private static void deleteUser() {
        while (true) {
            try {
                System.out.println("Enter user id to delete");
                final int userId = Integer.parseInt(scanner.nextLine());
                userDao.delete(userId);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter valid id");
            }
        }
    }

    private static void updateUser() {
        User user = null;

        while (true) {
            try {
                while (user == null) {
                    System.out.println("Enter user id to update user information:");
                    final int userId = Integer.parseInt(scanner.nextLine());
                    user = userDao.read(userId);

                    if (user == null) {
                        System.out.println("User with ID " + userId + " not found. Enter a valid user ID.");
                    }
                }

                while (true) {
                    System.out.println("Which part do you want to update? (username, email, password) Or 'quit' to cancel updates:");
                    final String inputUpdate = scanner.nextLine();

                    switch (inputUpdate) {
                        case "username" -> {
                            String newUsername = readUserInput("Enter new username", "Username cannot be null", input -> !input.isEmpty());
                            user.setUserName(newUsername);
                            System.out.println("Username changed.");
                        }
                        case "email" -> {
                            String newEmail = readUserInput("Enter new email", "Email cannot be null", input -> !input.isEmpty());
                            user.setEmail(newEmail);
                            System.out.println("Email changed.");
                        }
                        case "password" -> {
                            String newPasswd = readUserInput("Enter new password", "Password cannot be null", input -> !input.isEmpty());
                            user.setPassword(newPasswd);
                            System.out.println("Password changed.");
                        }
                        case "quit" -> {
                            userDao.update(user);
                            System.out.println(user);
                            System.out.println("User updated successfully.");
                            return;
                        }
                        default -> System.out.println("Invalid option selected.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a numeric user ID.");
            }
        }
    }


    public static void displayAllowedCommands(String[] commands) {
        System.out.println("Allowed commands: ");
        for (String command : commands) {
            System.out.println("# " + command);
        }
    }

    public static void createUser() {
        final String email = readUserInput("Enter email", "Email cannot be null", input -> !input.isEmpty());
        final String userName = readUserInput("Enter username", "Username cannot be null", input -> !input.isEmpty());
        final String passwd = readUserInput("Enter password", "password cannot be null", input -> !input.isEmpty());
        final User user = new User(userName, email, passwd);
        userDao.create(user);
        System.out.println("User created with given id: " + user.getId());
    }

    private static String readUserInput(String promptMessage, String errorMessage, Predicate<String> validator) {
        while (true) {
            System.out.println(promptMessage);
            String input = scanner.nextLine().trim();
            if (validator.test(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
}
