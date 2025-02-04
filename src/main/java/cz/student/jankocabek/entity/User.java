package cz.student.jankocabek.entity;

import cz.student.jankocabek.utils.PasswordHandler;

import java.util.Objects;

public class User {
    private int id;
    private String userName;
    private String passHash;
    private String email;
    private String salt;
    private static final PasswordHandler passwordHandler = PasswordHandler.getInstance();

    public User(int id, String userName, String email, String passHash, String salt) {
        this.id = id;
        this.userName = userName;
        this.passHash = passHash;
        this.email = email;
        this.salt = salt;
    }

    public User(String userName, String email, String password) {
        this.id = 0;
        this.userName = userName;
        this.email = email;
        this.passHash = passwordHandler.getHashedPass(password);
        this.salt = passwordHandler.getLastSalt();
    }

    public User(int id, String userName, String email, String password) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() && Objects.equals(getUserName(), user.getUserName()) && Objects.equals(getPassHash(), user.getPassHash()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getSalt(), user.getSalt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getPassHash(), getEmail(), getSalt());
    }
}
