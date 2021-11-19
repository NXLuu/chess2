/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.logicApplication;

import java.sql.PreparedStatement;
import model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.model.Client;

/**
 *
 * @author nxulu
 */
public class UserDAO extends DAO {

    public UserDAO() throws SQLException {
        super();
        this.connect();
    }

    public User Login(String username, String password) {
        User user = null;
        String sql = "SELECT *\n"
                + "FROM Users\n"
                + "WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int elo = rs.getInt("elo");
                int win = rs.getInt("win");
                int id = rs.getInt("id");
                user = new User(username, password, elo, win);
                user.setId(id);
            }
        } catch (SQLException e) {
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT *\n"
                + "FROM Users\n"
                +"ORDER BY elo DESC";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String userName = rs.getString("username");
                String password = rs.getString("password");
                int elo = rs.getInt("elo");
                int win = rs.getInt("win");
                int id = rs.getInt("id");

                User user = new User(userName, password, elo, win);
                user.setId(id);
                userList.add(user);
            }
        } catch (SQLException e) {
        }
        return userList;
    }

    public Client getUserByID(int id) {
        String sql = "SELECT *\n"
                + "FROM Users\n"
                + "WHERE id = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                return null;
            }
        } catch (SQLException e) {

        }
        return null;
    }

    public void UpdateElo(int id) {
        String sql = "Update Users SET elo = elo + 10 WHERE id = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
    
    public void UpdateWin(int id) {
        String sql = "Update Users SET win = win + 10 WHERE id = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
