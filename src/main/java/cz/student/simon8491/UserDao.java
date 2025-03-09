package cz.student.simon8491;

import cz.common.DbUtil;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final String CREATE_USER_QUERY = """
            INSERT INTO users (username, email, password) VALUES (?, ?, ?)""";
    private static final String READ_USER_QUERY = """
            SELECT * FROM users WHERE id = ?""";
    private static final String UPDATE_USER_QUERY = """
            UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?""";
    private static final String DELETE_USER_QUERY = """
            DELETE FROM users WHERE id = ?""";
    private static final String FIND_ALL_USERS_QUERY = """
            SELECT * FROM users""";

    public void createUser(User user) {
        try (Connection connection = DbUtil.getConnection()) {
            try (var stmtCreateUser = connection.prepareStatement
                    (CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtCreateUser.setString(1, user.getUserName());
                stmtCreateUser.setString(2, user.getEmail());
                stmtCreateUser.setString(3, hashPassword(user.getPassword()));
                stmtCreateUser.executeUpdate();
                try (var rsCreateUser = stmtCreateUser.getGeneratedKeys()) {
                    if (rsCreateUser.next()) {
                        user.setId(rsCreateUser.getInt(1));
                        System.out.println("Created user with ID: " + user.getId());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Problem with creating user in database:\n" + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User readUser(int id) {
        try (Connection connection = DbUtil.getConnection()) {
            try (var stmtReadUser = connection.prepareStatement(READ_USER_QUERY)) {
                stmtReadUser.setInt(1, id);
                try (var rsReadUser = stmtReadUser.executeQuery()) {
                    if (rsReadUser.next()) {
                        User user = new User();
                        user.setId(rsReadUser.getInt("id"));
                        user.setUserName(rsReadUser.getString("username"));
                        user.setEmail(rsReadUser.getString("email"));
                        user.setPassword(rsReadUser.getString("password"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("User cannot be read:\n" + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    public void updateUser(User user) {
        try (Connection connection = DbUtil.getConnection()) {
            try (var stmtUpdateUser = connection.prepareStatement(UPDATE_USER_QUERY)) {
                stmtUpdateUser.setString(1, user.getUserName());
                stmtUpdateUser.setString(2, user.getEmail());
                stmtUpdateUser.setString(3, this.hashPassword(user.getPassword()));
                stmtUpdateUser.setInt(4, user.getId());
                stmtUpdateUser.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Problem with updating user:\n" + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void deleteUser(int id) {
        try (Connection connection = DbUtil.getConnection()) {
            try (var stmtDeleteUser = connection.prepareStatement(DELETE_USER_QUERY)) {
                stmtDeleteUser.setInt(1, id);
                int affectedRows = stmtDeleteUser.executeUpdate();
                if (affectedRows > 0) {
                    System.out.printf("User with id %s deleted successfully.%n", id);
                } else {
                    System.out.printf("User delete with id %s failed. No rows affected.", id);
                }
            }
        } catch (SQLException e) {
            System.out.println("User cannot be deleted:\n" + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection()) {
            try (var stmtFindAll = connection.prepareStatement(FIND_ALL_USERS_QUERY)) {
                try (var rsFindAll = stmtFindAll.executeQuery()) {
                    while (rsFindAll.next()) {
                        User user = new User();
                        user.setId(rsFindAll.getInt("id"));
                        user.setUserName(rsFindAll.getString("username"));
                        user.setEmail(rsFindAll.getString("email"));
                        user.setPassword(rsFindAll.getString("password"));
                        users.add(user);
                    }
                    return users;
                }
            }
        } catch (SQLException e) {
            System.out.println("Users database problem:\n" + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }
}
