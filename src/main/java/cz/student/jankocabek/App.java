package cz.student.jankocabek;

import cz.student.jankocabek.Entity.DAO_Users;
import cz.student.jankocabek.Entity.Users;
import cz.student.jankocabek.utils.PasswordHandler;

public class App {
    public static void main(String[] args) {
        DAO_Users dao = new DAO_Users();
        System.out.printf("User:\n%s\n added", dao.create(new Users("test01", "test@seznam.cz", "147asdF")));
       System.out.printf("User:\n%s\n added", dao.create(new Users("test02", "ahoj@gmail.com", "qwerty257K")));
        System.out.printf("User:\n%s\n added", dao.create(new Users("test03", "strecha@info.org", "147asdF")));
        System.out.printf("User:\n%s\n added", dao.create(new Users("test04", "pepa@depo.info", "pepa77L")));
        System.out.printf("User:\n%s\n added", dao.create(new Users("test05", "marek82@outlook.com", "582465ALSK-")));

        for (Users users : dao.findAll()) {
            System.out.println(users);
        }
        System.out.println();

        Users updateUser = dao.findById(1);
        updateUser.setUserName("update3");
        updateUser.setEmail("update3@info.org");
        updateUser.setPassword("heslotest");
        dao.update(updateUser);
        System.out.println(dao.findById(1));
        System.out.println();

        System.out.printf("Was successfully deleted? %s",dao.delete(3));

        for (Users users : dao.findAll()) {
            System.out.println(users);
        }
        System.out.println();

        Users testPassUser = dao.findById(1);
        System.out.printf("is the password correct? : %s", PasswordHandler.getInstance().checkPassword("heslotest", testPassUser.getPassHash()));


        System.out.println();


    }
}
