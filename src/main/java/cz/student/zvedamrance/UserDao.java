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

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
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
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
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
        final int id = user.getId();
        try (Connection conn = DbUtil.getConnection(); Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter username: ");
            String username = sc.nextLine();
            System.out.println("Enter email: ");
            String email = sc.nextLine();
            System.out.println("Enter password: ");
            String password = sc.nextLine();
            PreparedStatement statement = conn.prepareStatement("UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?");
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, hashPassword(password));
            statement.setInt(4, id);
            statement.executeUpdate();
            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setId(id);
            System.out.println("Updated user: " + user.toString());
        } catch (SQLException e ) {
            System.out.println("Error, user update failed.");
            throw new RuntimeException();
        }
    }
    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, userId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with id %s deleted successfully.".formatted(userId));
            } else {
                System.out.println("User delete with id %s failed. No rows affected.".formatted(userId));
            }
        } catch (SQLException e) {
            System.out.println("Error, user deletion failed.");
            throw new RuntimeException();
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
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(id, username, email, password);
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new User[list.size()]);
    }
}
