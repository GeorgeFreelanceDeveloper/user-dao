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
    public static final String CREATE_USER =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ? )";


    public User create(User user) { //metoda pro vytvareni uzivatele
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                System.out.println("User ID " + user.getId());

            }
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR while creating a user");
            return null;

        }
    }

    public static final String UPDATE_USER =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

    public void update(User user, Connection conn) { //metoda pro aktualizovani inforamci uzivatele
        try {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER);

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());

            statement.executeUpdate();
            System.out.println("Changes applied to user with ID: " + user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR while updating the user");
        }
    }

    public static final String READ_USER =
            "SELECT * FROM users WHERE id = ?";

    public User read(int userId, Connection conn) { //metoda pro precteni informaci uzivatele

        try {
            PreparedStatement statement = conn.prepareStatement(READ_USER);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
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
            e.printStackTrace();
            System.err.println("ERROR while reading the user");
        }
        return null;
    }

    public static final String DELETE_USER =
            "DELETE FROM users WHERE id = ?";

    public void delete(int userId, Connection conn) { // metoda pro smazani uzivatele
        try {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER);
            statement.setInt(1, userId);
            statement.executeUpdate();
            System.out.println("User with ID: " + userId + " has been removed");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR while deleting the user");
        }
    }

    public static final String READ_ALL_USERS =
            "SELECT * FROM users";

    public List<User> findAll(Connection conn) {  //metoda pro zobrazeni vsech uctu
        try {
            List<User> users = new ArrayList<>();
            PreparedStatement statement = conn.prepareStatement(READ_ALL_USERS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR while reading all the users");
        }
        return null;
    }


    public String hashPassword(String password) { // zabezpecovaci metoda hesel
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
