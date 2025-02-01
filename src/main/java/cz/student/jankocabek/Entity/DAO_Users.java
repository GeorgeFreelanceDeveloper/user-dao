package cz.student.jankocabek.Entity;

import cz.common.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DAO_Users {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password,salt) VALUES (?, ?, ?,?)";
    private static final String FIND_USER_QUERY =
            "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ?, salt=? WHERE  id = ?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";
    private static final String FINDALL_USER_QUERY =
            "SELECT * FROM users";
    private static final String FIND_LAST_USER_QUERY =
            "SELECT * FROM users ORDER BY id DESC LIMIT 1";

    public Users create(Users user) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassHash());
            ps.setString(4, user.getSalt());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Users findById(int id) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(FIND_USER_QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(rs.getInt(1),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("salt"));
            }
            return null;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void update(Users user) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(UPDATE_USER_QUERY);
            ps.setInt(5, user.getId());
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassHash());
            ps.setString(4, user.getSalt());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean delete(int userId) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(DELETE_USER_QUERY);
            ps.setInt(1, userId);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public List<Users> findAll() {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(FINDALL_USER_QUERY);
            ResultSet rs = ps.executeQuery();
            List<Users> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new Users(rs.getInt(1),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("salt")));

            }
            return users;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Users findLast() {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(FIND_LAST_USER_QUERY);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(rs.getInt(1), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("salt"));
            }
            return null;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
