package cz.student.zvedamrance;

import cz.common.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {

        final UserDao userDao = new UserDao();
        User user1 = new User("Charlie", "charlie@gmail.com", "heslo125" );
        User user2 = new User("Dee", "dee@gmail.com", "pass75" );
        User user3 = new User("Frank", "frank@gmail.com", "bdiiya" );

        //Testy metody create

        //userDao.create(user1);
        //userDao.create(user2);
        //userDao.create(user3);

        //Test metody update

        //userDao.update(user1);

        //Test metody readAll

        User [] listOfAll = userDao.readAll();
        for (User user : listOfAll) {
            System.out.println(user);
        }

        //Test metody read
        System.out.println(userDao.read(3));
    }
}
