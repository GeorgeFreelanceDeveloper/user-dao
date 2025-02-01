package cz.student.osydorchuk89;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public User createUser(String userName, String email, String password) {

        String createUserQuery = "insert into users (username, email, password) values (?, ?, ?)";

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(createUserQuery, Statement.RETURN_GENERATED_KEYS);
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
            e.printStackTrace(System.err);
            return null;
        }
    }

    public User getUserById(int id) {

        String getUserByIdQuery = "select * from users where id = ?";

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(getUserByIdQuery);
            statement.setString(1, Integer.toString(id));
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new User(result.getInt("id"), result.getString("username"), result.getString("email"), result.getString("password"));
            } else {
                System.out.println("No user with the given id was found");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public void updateUser(User user, String userName, String email, String password) {
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);

        String updateUserQuery = "update users set username = ?, email = ?, password = ? where id = ?";

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(updateUserQuery);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setString(4, Integer.toString(user.getId()));
            statement.executeUpdate();
            System.out.println("User " + user.getUserName() + " updated successfully");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void deleteUserById(int id) {
        User user = getUserById(id);

        String deleteUserByIdQuery = "delete from users where id = ?";

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(deleteUserByIdQuery);
            statement.setString(1, Integer.toString(user.getId()));
            statement.executeUpdate();
            System.out.println("User " + user.getUserName() + " deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public List<User> getAllUsers() {
        String getAllUsersQuery = "select * from users";

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(getAllUsersQuery);
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
