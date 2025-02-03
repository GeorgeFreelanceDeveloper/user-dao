package cz.student.richard808code;

import cz.common.DbUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDao {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE id = ?";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";

    private static final String SELECT_ALL_USERS_QUERY =
            "SELECT * FROM users";

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {

            final PreparedStatement preStmt = conn.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getEmail());
            preStmt.setString(3, hashPassword(user.getPassword()));
            preStmt.executeUpdate();

            final ResultSet rs = preStmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Inserted ID: " + id);
                user.setId(id);
                System.out.println("Created user with ID: " + id);
            }
            return user;

        } catch (SQLException e) {
            System.err.println("Problem with creating user in database: " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    public User read(int userId) {

        try (Connection connection = DbUtil.getConnection()) {
            final PreparedStatement preStmt = connection.prepareStatement(READ_USER_QUERY);
            preStmt.setInt(1, userId);

            final ResultSet rs = preStmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }
            return user;

        } catch (SQLException e) {
            System.err.println("Could not read user with ID: " + userId + "from database." + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    public void update(User user) {

        try (Connection connection = DbUtil.getConnection()) {
            final PreparedStatement preStmt = connection.prepareStatement(UPDATE_USER_QUERY);
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getEmail());
            preStmt.setString(3, hashPassword(user.getPassword()));
            preStmt.setInt(4, user.getId());

            final int affectedRows = preStmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User with id " + user.getId() + " updated successfully.");
            } else {
                System.out.println("User update failed. No rows affected.");
            }

        } catch (SQLException e) {
            System.err.println("Could not update user with id " + user.getId() + " in database: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void delete(int userId) {
        try (Connection connection = DbUtil.getConnection()) {
            final PreparedStatement preStmt = connection.prepareStatement(DELETE_USER_QUERY);
            preStmt.setInt(1, userId);

            final int affectedRows = preStmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User with id " + userId + " deleted successfully.");
            } else {
                System.out.println("User with id " + userId + "deletion failed. No rows affected.");
            }

        } catch (SQLException e) {
            System.err.println("Could not delete user with id " + userId + "from database: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public User[] findAll() {
        User[] users = new User[0];
        try (Connection connection = DbUtil.getConnection()) {
            final PreparedStatement preStmt = connection.prepareStatement(SELECT_ALL_USERS_QUERY);
            final ResultSet rs = preStmt.executeQuery();

            while (rs.next()) {
                final User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                users = addToArray(user, users);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Cannot connect to database");
            e.printStackTrace();
        }
        return users;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }
}
