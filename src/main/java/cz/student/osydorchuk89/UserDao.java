package cz.student.osydorchuk89;

import cz.common.DbUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM USERS WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";

    public User createUser(String userName, String email, String password) {

        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userName);
            statement.setString(2, email);
            statement.setString(3, hashPassword(password));
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                System.out.println("User " + userName + " created successfully");
                return new User(result.getInt(1), userName, email, password);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Problem with adding user in the database: " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    public User getUserById(int userId) {

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(GET_USER_BY_ID_QUERY);
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new User(result.getInt("id"), result.getString("username"), result.getString("email"), result.getString("password"));
            } else {
                System.out.println("No user with the given id was found");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Problem with retrieving user from the database: " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    public void updateUser(int id, String userName, String email, String password) {

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, userName);
            statement.setString(2, email);
            statement.setString(3, hashPassword(password));
            statement.setString(4, Integer.toString(id));
            statement.executeUpdate();
            System.out.println("User " + userName + " updated successfully");
        } catch (SQLException e) {
            System.err.println("Problem with updating user in the database: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void deleteUserById(int userId) {

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.printf("User with id %s deleted successfully.%n", userId);
            } else {
                System.out.printf("User delete with id %s failed. No rows affected.%n", userId);
            }
        } catch (SQLException e) {
            System.err.println("Problem with deleting user from the database: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public List<User> getAllUsers() {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(GET_ALL_USERS_QUERY);
            ResultSet result = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (result.next()) {
                users.add(new User(result.getInt("id"), result.getString("username"), result.getString("email"), result.getString("password")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
