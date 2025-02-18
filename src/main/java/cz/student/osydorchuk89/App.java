package cz.student.osydorchuk89;

import java.util.List;

public class App {

    public static void main(String[] args) {
        final UserDao userDao = new UserDao();
        System.out.println("Creating new users");
        User user1  = userDao.createUser("user1", "user1@gmail.com", "password1");
        User user2 = userDao.createUser("user2", "user2@gmail.com", "password2");
        User user3 = userDao.createUser("user3", "user3@gmail.com", "password3");
        System.out.println("\nGetting user with id " + user3.getId());
        User user3Copy = userDao.getUserById(user3.getId());
        System.out.println(user3Copy);
        System.out.println("\nUpdating user with id " + user1.getId());
        userDao.updateUser(user1.getId(), "user1_upd", "user1@gmail.com_upd", "password1_upd");
        System.out.println("\nDeleting user with id " + user3.getId());
        userDao.deleteUserById(user3.getId());
        System.out.println("\nListing all users");
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

}
