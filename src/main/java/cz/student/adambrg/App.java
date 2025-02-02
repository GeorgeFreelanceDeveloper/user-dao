package cz.student.adambrg;


import cz.common.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class App {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = DbUtil.getConnection()) {
            System.out.println("Connected to the database");
            //Test vytvoreni uzivatele
            UserDao userDao = new UserDao();
            User user = new User();
            user.setUserName("Karel");
            user.setEmail("karel.novak4@seznam.cz");
            user.setPassword("kolobezka");
            userDao.create(user);

            User secondUser = new User();
            secondUser.setUserName("Jan");
            secondUser.setEmail("jan.novotny4@seznam.cz");
            secondUser.setPassword("autobus");
            userDao.create(secondUser);

            //Test precteni dat uzivatele
            User read = userDao.read(2, conn);
            System.out.println("Data uzivatele" + read);
            // Test aktualizace udaju uzivatele
            User userToUpdate = userDao.read(2, conn);
            userToUpdate.setUserName("kaja");
            userToUpdate.setEmail("kajanovak@seznam.cz");
            userToUpdate.setPassword("modrakolobezka");
            userDao.update(userToUpdate, conn);

            // Vypis vsech uzivatelu
            System.out.println("Data vsech uzivatelu:");
            List<User> allUsers = userDao.findAll(conn);

            if (allUsers != null && !allUsers.isEmpty()) {
                for (User u : allUsers) {
                    System.out.println(u);
                }
            } else {
                System.out.println("No users found.");

                //Test smazani uzivatelu

                userDao.delete(4, conn);
                userDao.delete(2, conn);


            }
        } catch (SQLException e) {
            System.err.println("Connection failed");
            e.printStackTrace();
        }
    }
}




