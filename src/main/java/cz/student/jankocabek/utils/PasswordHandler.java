package cz.student.jankocabek.utils;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordHandler {

    private String hash;
    private String salt;


    private static PasswordHandler instance;

    public static PasswordHandler getInstance() {
        if (instance == null) {
            instance = new PasswordHandler();
        }
        return instance;
    }

    private PasswordHandler() {
    }

    public String getHashedPass(String password) {
        hash = BCrypt.hashpw(password, generateSalt());
        return hash;
    }

    private String generateSalt() {
        salt = BCrypt.gensalt();
        return salt;
    }

    public String getLastSalt() {
        return salt;
    }

    public boolean checkPassword(String password, String hashedPass) {
        return BCrypt.checkpw(password, hashedPass);
    }

}
