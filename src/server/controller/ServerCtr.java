/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.History;
//import logicAplication.UserDAO;
import model.Message;
import model.ReqFr;
import server.logicApplication.UserDAO;
import server.model.Client;
import model.User;

/**
 *
 * @author X550V
 */
public class ServerCtr {

    public static ServerSocket serverSocket;
    public static int IdClient = 1000;
    public static int portAudio = 60000;
    public static int IdRoom = 0;
    public static int port = 0;
    public static NewClientListener runThread;
    public static ArrayList<Client> Clients = new ArrayList<>();

    public static void Start(int openport) {
        try {
            ServerCtr.port = openport;
            ServerCtr.serverSocket = new ServerSocket(ServerCtr.port);

            ServerCtr.runThread = new NewClientListener();
            ServerCtr.runThread.start();

        } catch (IOException ex) {
            Logger.getLogger(ServerCtr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeClient(Client cl) {
        Clients.remove(cl);
    }

    public static void Send(Client cl, Message msg) {

        try {
            cl.getsOutput().writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sendBroadCast(Message msg) {
        try {
            for (Client c : Clients) {
                c.getsOutput().writeObject(msg);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Client getClientById(int id) {
        for (Client c : Clients) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public static ArrayList<String> ReturnRooms() {
        ArrayList<String> rooms = new ArrayList<>();

        Clients.stream().filter((c) -> (c.getRoomId() != -1)).forEachOrdered((c) -> {
            if (c.getOpponent() == null) {
                System.out.println(c.getOpponent());
                rooms.add(c.getRoomName());
            }
        });
        return rooms;
    }

    public static Client JoinRoom(String roomName) {
        for (Client c : Clients) {
            if (c.getRoomName().equals(roomName)) {
                return c;
            }
        }
        return null;
    }

    public static User CheckLogin(String userName, String password) throws java.sql.SQLException {
        UserDAO userDao = new UserDAO();

        User user = userDao.Login(userName, password);
        return user;
    }

    public static void Sigup(String userName, String password) throws java.sql.SQLException {
        UserDAO userDao = new UserDAO();

        userDao.insertUser(userName, password);

    }

    public static List<History> getAllHis(int id) throws SQLException {

        UserDAO userDao = new UserDAO();
        List<History> his = userDao.getHistory(id);
        return his;
    }

    public static List<User> getAllUsers() throws SQLException {

        UserDAO userDao = new UserDAO();
        List<User> userList = userDao.getAllUsers();
        return userList;
    }

    public static List<ReqFr> getAllFriendReq(int id) throws SQLException {
        UserDAO userDao = new UserDAO();
        return userDao.getFriendReq(id);
    }

    public static List<User> updateElo() throws SQLException {
        UserDAO userDao = new UserDAO();
        List<User> userList = userDao.getAllUsers();
        return userList;
    }

}
