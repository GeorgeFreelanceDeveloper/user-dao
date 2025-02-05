package cz.student.zvedamrance;

import cz.common.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            Connection connection = DbUtil.getConnection();
            System.out.println("Connected to database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        UserDao userDao = new UserDao();
        User user1 = new User("Charlie", "charlie@gmail.com", "heslo125" );
        User user2 = new User("Dee", "dee@gmail.com", "pass75" );
        User user3 = new User("Frank", "frank@gmail.com", "bdiiya" );
        //userDao.create(user1);
        //userDao.create(user2);
        //userDao.create(user3);
        //userDao.update(user1);
        User [] xd = userDao.readAll();
        for (User user : xd) {
            System.out.println(user);
        }
        System.out.println(userDao.read(3));
    }
}
