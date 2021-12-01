/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.logicApplication;

import java.sql.Date;
import java.sql.PreparedStatement;
import model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.History;
import model.ReqFr;
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

    public void insertHistery(int userId, String opponentName, boolean isWin) {
        String sql = "INSERT INTO `chess`.`history` (`usersId`, `date`, `oponentName`, `win`) VALUES (?, ?, ?, ?); ";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setDate(2, new Date(System.currentTimeMillis()));
            ps.setString(3, opponentName);
            if (isWin) {
                ps.setString(4, "Win");
            } else {
                ps.setString(4, "Loss");
            }
            ps.execute();

        } catch (SQLException e) {
        }

    }

    public void insertUser(String username, String password) {
        String sql = "INSERT INTO `chess`.`users` (`username`, `password`, `elo`, `win`) VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.execute();

        } catch (SQLException e) {
        }

    }

    public List<User> getFr(int id) {
        List<User> frs = new ArrayList<>();
        String sql = "SELECT * FROM friend WHERE idMe = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idFr = rs.getInt("idFriend");
                String sql1 = "SELECT * FROM users WHERE id = ?";
                PreparedStatement ps1 = jdbcConnection.prepareStatement(sql1);
                ps1.setInt(1, idFr);
                ps1.execute();
                ResultSet rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    String userName = rs1.getString("username");
        
                    int elo = rs1.getInt("elo");
                    int win = rs1.getInt("win");

                    User user = new User(userName, "", elo, win);
                    user.setId(idFr);
                    frs.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return frs;
    }

    public void createFrReq(int idRev, int idSend) {
        String sql = "INSERT INTO `chess`.`request` (`idSend`, `idRev`, `date`) VALUES (?, ?, ?); ";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, idSend);
            ps.setInt(2, idRev);
            ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createFriendShip(int id1, int id2) {
        String sql = "INSERT INTO `chess`.`friend` (`idMe`, `idFriend`) VALUES (?, ?);";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteFr(int id) {
        String sql = "DELETE FROM request WHERE id = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReqFr getFriendReqById(int id) {
        List<ReqFr> reqFr = new ArrayList<>();
        String sql = "SELECT * FROM request WHERE id = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return new ReqFr(null, null, id, rs.getInt("idSend"), rs.getInt("idRev"));
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<ReqFr> getFriendReq(int id) {
        List<ReqFr> reqFr = new ArrayList<>();
        String sql = "SELECT * FROM request WHERE idRev = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idReq = rs.getInt("id");
                int idSend = rs.getInt("idSend");
                Date date = rs.getDate("date");
                String sql1 = "SELECT * FROM users WHERE id = ?";
                PreparedStatement ps1 = jdbcConnection.prepareStatement(sql1);
                ps1.setInt(1, idSend);
                ps1.execute();
                ResultSet rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    String userName = rs1.getString("username");
                    reqFr.add(new ReqFr(userName, date, idReq, idSend, rs.getInt("idRev")));
                }
            }

        } catch (SQLException e) {
        }

        return reqFr;
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
                + "ORDER BY elo DESC";
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

    public List<User> search(String name) {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT *\n"
                + "FROM Users\n"
                + "WHERE username like ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("username");
                User user = new User();
                user.setId(id);
                user.setUserName(userName);
                userList.add(user);
            }
        } catch (SQLException e) {
        }
        return userList;
    }

    public User getUserByID(int id) {
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

    public List<History> getHistory(int id) throws SQLException {
        List<History> historys = new ArrayList<>();
        String sql = "SELECT *\n"
                + "FROM history WHERE usersId = ?\n"
                + "ORDER BY date DESC";
        PreparedStatement ps = jdbcConnection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String oponentName = rs.getString("oponentName");
            String win = rs.getString("win");
            Date date = rs.getDate("date");

            historys.add(new History(date, oponentName, win));
        }
        return historys;
    }

    public void UpdateWin(int id) {
        String sql = "Update Users SET win = win + 1 WHERE id = ?";
        try {
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
