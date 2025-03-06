package cz.student.adambrg;

import cz.common.DbUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final String CREATE_USER =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ? )";


    public User create(User user) { //metoda pro vytvareni uzivatele
        try (Connection conn = DbUtil.getConnection()) {
           final PreparedStatement statement = conn.prepareStatement(CREATE_USER, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();

           final ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                System.out.println("User ID " + user.getId());

            }
            return user;

        } catch (SQLException e) {
            System.err.println("ERROR while creating a user");
            e.printStackTrace();
            return null;

        }
    }

    private static final String UPDATE_USER =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

    public void update(User user) { //metoda pro aktualizovani inforamci uzivatele
        try (Connection conn = DbUtil.getConnection()){
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
        } catch (SQLException e) {
            System.err.println("ERROR while updating the user");
            e.printStackTrace();
        }
    }

    private static final String READ_USER =
            "SELECT * FROM users WHERE id = ?";

    public User read(int userId) { //metoda pro precteni informaci uzivatele

        try (Connection conn = DbUtil.getConnection()){
           final PreparedStatement statement = conn.prepareStatement(READ_USER);
            statement.setInt(1, userId);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                System.out.println("Information about user:");
                return user;

            }
        } catch (SQLException e) {
            System.err.println("ERROR while reading the user");
            e.printStackTrace();
        }
        return null;
    }

    private static final String DELETE_USER =
            "DELETE FROM users WHERE id = ?";

    public void delete(int userId) { // metoda pro smazani uzivatele
        try (Connection conn = DbUtil.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement(DELETE_USER);
            statement.setInt(1, userId);
           final int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User with ID: " + userId + " has been successfully removed.");
            } else {
                System.out.println("Failed to remove user with ID: " + userId + ".");
            }

        } catch (SQLException e) {
            System.err.println("ERROR while deleting the user");
            e.printStackTrace();
        }
    }

    private static final String READ_ALL_USERS =
            "SELECT * FROM users";

    public List<User> findAll() {  //metoda pro zobrazeni vsech uctu
        try (Connection conn = DbUtil.getConnection()){
            final List<User> users = new ArrayList<>();
            final PreparedStatement statement = conn.prepareStatement(READ_ALL_USERS);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            System.err.println("ERROR while reading all the users");
            e.printStackTrace();
        }
        return null;
    }


    private String hashPassword(String password) { // zabezpecovaci metoda hesel
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
