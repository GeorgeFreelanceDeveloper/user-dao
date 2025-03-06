package cz.student.adambrg;


import cz.common.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class App {
    public static void main(String[] args) throws SQLException {


        //Test vytvoreni uzivatele
        final UserDao userDao = new UserDao();
        final User user = new User();
        user.setUserName("Karel");
        user.setEmail("karel.novak4@seznam.cz");
        user.setPassword("kolobezka");
        userDao.create(user);

        System.out.println("User created with ID:" + user.getId());

        final User secondUser = new User();
        secondUser.setUserName("Jan");
        secondUser.setEmail("jan.novotny4@seznam.cz");
        secondUser.setPassword("autobus");
        userDao.create(secondUser);

        System.out.println("User created with ID:" + secondUser.getId());

        //Test precteni dat uzivatele
        final User read = userDao.read(2);
        System.out.println("Data of user with ID: " + read.getId() + ": " + read);
        // Test aktualizace udaju uzivatele
        final User userToUpdate = userDao.read(2);
        userToUpdate.setUserName("kaja");
        userToUpdate.setEmail("kajanovak@seznam.cz");
        userToUpdate.setPassword("modrakolobezka");
        userDao.update(userToUpdate);


        System.out.println("User with ID " + userToUpdate.getId() + " updated.");

        // Vypis vsech uzivatelu
        System.out.println("Data from all users: ");
        final List<User> allUsers = userDao.findAll();

        if (allUsers != null && !allUsers.isEmpty()) {
            for (User u : allUsers) {
                System.out.println(u);
            }
        } else {
            System.out.println("No users found.");

        }
        //Test smazani uzivatelu
            userDao.delete(4);
            userDao.delete(2);

            System.out.println("User with ID: " + user.getId() + "got DELETED from the DATABASE" );
            System.out.println("User with ID: " + user.getId() + "got DELETED from the DATABASE" );




    }
}




