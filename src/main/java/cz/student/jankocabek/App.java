package cz.student.jankocabek;

import cz.student.jankocabek.entity.User;
import cz.student.jankocabek.entity.UserDao;

public class App {
    public static void main(String[] args) {
        final UserDao dao = new UserDao();
        System.out.printf("User:\n%s\n added", dao.create(new User("test01", "test1@seznam.cz", "147asdF")));
        System.out.printf("User:\n%s\n added", dao.create(new User("test02", "ahoj@gmail.com", "qwerty257K")));
        System.out.printf("User:\n%s\n added", dao.create(new User("test03", "strecha@info.org", "147asdF")));
        System.out.printf("User:\n%s\n added", dao.create(new User("test04", "pepa@depo.info", "pepa77L")));
        System.out.printf("User:\n%s\n added", dao.create(new User("test05", "marek82@outlook.com", "582465ALSK-")));

        for (User users : dao.findAll()) {
            System.out.println(users);
        }
        System.out.println();

        User updateUser = dao.findById(1);
        updateUser.setUserName("update3");
        updateUser.setEmail("update3@info.org");
        updateUser.setPassword("heslotest");
        dao.update(updateUser);
        System.out.printf("Find user with ID 1: %s%n", dao.findById(1));
        System.out.println();

        System.out.printf("Was user with ID 3 successfully deleted? %s", dao.delete(3));

        for (User users : dao.findAll()) {
            System.out.println(users);
        }
        System.out.println();

        User testPassUser = dao.findById(1);
        System.out.printf("is the password correct? : %s\n", testPassUser.checkPassword("heslotest"));
        System.out.println();
        dao.create(new User("a", "b@gml.com", "123"));
        User findUser = dao.findById(26);
        System.out.printf("is the password correct? : %s\n", findUser.checkPassword("123"));

    }
}
