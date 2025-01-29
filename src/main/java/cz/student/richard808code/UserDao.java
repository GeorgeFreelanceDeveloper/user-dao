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

            PreparedStatement preStmt = conn.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getEmail());
            preStmt.setString(3, hashPassword(user.getPassword()));
            preStmt.executeUpdate();

            ResultSet rs = preStmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Inserted ID: " + id);
                user.setId(id);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Cannot connect to database");
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public User read(int userId) {
        User user = null;
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preStmt = connection.prepareStatement(READ_USER_QUERY);
            preStmt.setInt(1, userId);

            ResultSet rs = preStmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Cannot connect to database");
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public void update(User user) {
        User updatedUser = null;
        UserDao userDao = new UserDao();
        updatedUser = userDao.read(user.getId());

        if (updatedUser != null) {
            updatedUser.setUserName(user.getUserName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword((hashPassword(user.getPassword())));
        }

        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preStmt = connection.prepareStatement(UPDATE_USER_QUERY);
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getEmail());
            if (!updatedUser.getPassword().equals(user.getPassword())) {
                preStmt.setString(3, hashPassword(user.getPassword()));
            }
            preStmt.setInt(4, user.getId());

            int affectedRows = preStmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User update failed. No rows affected.");
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Cannot connect to database");
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preStmt = connection.prepareStatement(DELETE_USER_QUERY);
            preStmt.setInt(1, userId);

            int affectedRows = preStmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User delete failed. No rows affected.");
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Cannot connect to database");
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        User [] users = new User[0];
        try(Connection connection = DbUtil.getConnection()){
            PreparedStatement preStmt = connection.prepareStatement(SELECT_ALL_USERS_QUERY);
            ResultSet rs = preStmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                users = addToArray(user, users);
            }

        }catch (SQLException e){
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
