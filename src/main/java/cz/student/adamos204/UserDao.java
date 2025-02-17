package cz.student.adamos204;

import cz.common.DbUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(email, username, password) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, username = ?, password= ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    private String hashPasswd(String passwd) {
        return BCrypt.hashpw(passwd, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, hashPasswd(user.getPassword()));
            statement.executeUpdate();
            final ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            System.err.println("Problem with creating user " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY);
            statement.setInt(1, userId);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("userName"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Problem with retrieving user " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
        return null;
    }

    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, this.hashPasswd(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Problem with updating user " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0){
                System.out.println("User not deleted, id does not exist");
            } else {
                System.out.println("User deleted");
            }
        } catch (SQLException e) {
            System.err.println("Problem with deleting user " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public User[] readAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(SELECT_ALL_USERS_QUERY);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(); // Create a new User object for each row
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Problem with retrieving all users " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return users.toArray(new User[0]);
    }
}
