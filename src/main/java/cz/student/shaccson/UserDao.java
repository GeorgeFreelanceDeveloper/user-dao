package cz.student.shaccson;
import cz.common.DbUtil;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public User addUser(final User user) {
        try (final Connection conn = DbUtil.getConnection();
             final PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();

            try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        } catch (final SQLException e) {
            System.err.println("Error while adding user: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    public User getUserById(final int id) {
        try (final Connection conn = DbUtil.getConnection();
             final PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY)) {

            statement.setInt(1, id);
            try (final ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    final User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        } catch (final SQLException e) {
            System.err.println("Error while retrieving user by ID: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    public void updateUser(final User user) {
        try (final Connection conn = DbUtil.getConnection();
             final PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            final int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with ID " + user.getId() + " updated successfully.");
            } else {
                System.out.println("User update failed. No rows affected.");
            }
        } catch (final SQLException e) {
            System.err.println("Error while updating user: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void deleteUser(final int id) {
        try (final Connection conn = DbUtil.getConnection();
             final PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY)) {
            statement.setInt(1, id);
            final int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with ID %d deleted successfully.".formatted(id));
            } else {
                System.out.println("User delete with ID %d failed. No rows affected.".formatted(id));
            }
        } catch (final SQLException e) {
            System.err.println("Error while deleting user: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public List<User> getAllUsers() {
        final List<User> users = new ArrayList<>();
        try (final Connection conn = DbUtil.getConnection();
             final PreparedStatement statement = conn.prepareStatement(SELECT_ALL_USERS_QUERY);
             final ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                final User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (final SQLException e) {
            System.err.println("Error while fetching all users: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return users;
    }

    private String hashPassword(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
