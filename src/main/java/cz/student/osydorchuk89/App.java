package cz.student.osydorchuk89;

import java.util.List;

public class App {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User user1  = userDao.createUser("user1", "user1@gmail.com", "password1");
        User user2 = userDao.createUser("user2", "user2@gmail.com", "password2");
        User user3 = userDao.createUser("user3", "user3@gmail.com", "password3");
        userDao.updateUser(user1, "user1_upd", "user1@gmail.com_upd", "password1_upd");
        userDao.deleteUserById(user3.getId());
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }

    }

}
