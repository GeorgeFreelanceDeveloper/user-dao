package cz.student.jankocabek.Entity;

import cz.student.jankocabek.utils.PasswordHandler;

public class Users {
    private int id;
    private String userName;
    private String passHash;
    private String email;
    private String salt;
    private static final PasswordHandler passwordHandler = PasswordHandler.getInstance();

    public Users(int id, String userName, String email,String passHash, String salt) {
        this.id = id;
        this.userName = userName;
        this.passHash = passHash;
        this.email = email;
        this.salt = salt;
    }

    public Users(String userName, String email, String password) {
        this.id = 0;
        this.userName = userName;
        this.email = email;
        this.passHash = passwordHandler.getHashedPass(password);
        this.salt = passwordHandler.getLastSalt();
    }

    public Users(int id, String userName, String email, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.passHash = passwordHandler.getHashedPass(password);
        this.salt = passwordHandler.getLastSalt();
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassHash() {
        return passHash;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.passHash = passwordHandler.getHashedPass(password);
        this.salt = passwordHandler.getLastSalt();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passHash='" + passHash + '\'' +
                ", email='" + email + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
