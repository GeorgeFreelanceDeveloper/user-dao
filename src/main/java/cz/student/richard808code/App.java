package cz.student.richard808code;

public class App {
    public static void main(String[] args) {
        final UserDao userDao = new UserDao();

        // Vytvoření uživatele

        final User user1 = new User();
        user1.setUserName("username1");
        user1.setEmail("email1@gmail.com");
        user1.setPassword("password1");

        //Test metody create
        final User createdUser1 = userDao.create(user1);

        System.out.println("User created with ID: " + createdUser1.getId());


        //Test metody read pro existující záznam

        System.out.println("Result of finding user with id = 1: " + userDao.read(1));

        //Test metody read pro neexistující záznam

        System.out.println("Result of finding user with id = 2: " + userDao.read(2));

        //Test metody update

        user1.setUserName("username2");
        user1.setEmail("email2@gmail.com");
        user1.setPassword("password2");
        userDao.update(user1);

        //Test metody findAll

        final User[] users = userDao.findAll();

        System.out.println("Number of users: " + users.length);

        System.out.println("First user: " + users[0].getUserName() + ", " + users[0].getEmail());

        //Test metody delete

        userDao.delete(user1.getId());
    }
}
