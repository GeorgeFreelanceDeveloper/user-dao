package cz.student.shaccson;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection conn;

    public UserDao(final Connection conn) {
        this.conn = conn;
    }

    public void addUser(final User user) {
        final String query = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
        try (final PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();

            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(final int id) {
        final String query = "SELECT * FROM users WHERE id = ?";
        try (final PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                final User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(final User user) {
        final String query = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";
        try (final PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(final int id) {
        final String query = "DELETE FROM users WHERE id = ?";
        try (final PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            final int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with id %s deleted successfully.".formatted(id));
            } else {
                System.out.println("User delete with id %s failed. No rows affected.".formatted(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        final List<User> users = new ArrayList<>();
        final String query = "SELECT * FROM users";
        try (final PreparedStatement statement = conn.prepareStatement(query);
             final ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                final User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}