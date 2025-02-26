package cz.student.zvedamrance;

import cz.common.DbUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_USER_BY_ID =
            "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER =
            "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT * FROM users";


    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            final ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("Created user: " + user.toString());
            return user;
        } catch (SQLException e) {
            System.out.println("Error, user creation failed.");
            e.printStackTrace();
            return null;
        }
    }
    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(SELECT_USER_BY_ID);
            statement.setInt(1, userId);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                System.out.println("Found user with user ID " + userId);
                return new User(userId,username, email, password);
            } else {
                System.out.println("User with id " + userId + " not found.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error, user read failed.");
            throw new RuntimeException(e);
        }
    }
    public void update(User user) {
        try (Connection conn = DbUtil.getConnection(); Scanner sc = new Scanner(System.in)) {

            final PreparedStatement statement = conn.prepareStatement(UPDATE_USER);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());

            final int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with id " + user.getId() + " updated successfully.");
            } else {
                System.out.println("User update failed. No rows affected.");
            }
        } catch (SQLException e ) {
            System.out.println("Error, user update failed.");
            throw new RuntimeException();
        }
    }
    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(DELETE_USER);
            statement.setInt(1, userId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with id %s deleted successfully.".formatted(userId));
            } else {
                System.out.println("User delete with id %s failed. No rows affected.".formatted(userId));
            }
        } catch (SQLException e) {
            System.out.println("Error, user deletion failed. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }
    public User [] readAll() {
        ArrayList<User> list = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(SELECT_ALL);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                final String username = resultSet.getString("username");
                final String email = resultSet.getString("email");
                final String password = resultSet.getString("password");
                final User user = new User(id, username, email, password);
                list.add(user);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Cannot connect to database");
            e.printStackTrace();
        }
        return list.toArray(new User[list.size()]);
    }
}
