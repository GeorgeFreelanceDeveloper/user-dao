package cz.student.adamos204;

public class App {

    private static final UserDao userDao = new UserDao();

    public static void main(String[] args){
        //final User user = new User("LOL", "new@gmail.com", "testing");
        //userDao.create(user);
        //System.out.println(userDao.read(3));
        //final User updateUser = userDao.read(1);
        //updateUser.setUserName("changeName");
        //updateUser.setEmail("change@email.com");
        //updateUser.setPassword("totallyDifferentPassword");
        //userDao.update(updateUser);
        //userDao.delete(1);
        userDao.readAll();
    }
}
