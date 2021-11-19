/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import server.model.Client;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;
import model.User;
import server.logicApplication.UserDAO;

/**
 *
 * @author X550V
 */
public class ClientListener extends Thread {

    Client TheClient;

    public ClientListener(Client TheClient) {
        this.TheClient = TheClient;
    }

    @Override
    public void run() {
        while (TheClient.getSoket().isConnected()) {
            try {
                try {
                    Message msg = (Message) TheClient.getsInput().readObject();
                    String name;
                    String password;
                    switch (msg.type) {
                        case JoinServer:
                            name = msg.content.toString();

                            TheClient.setName(name);
                            System.out.println("User " + name + " has joined the server...");
                            break;
                        case Login:
                            ArrayList inforUser = (ArrayList) msg.content;
                            name = inforUser.get(0).toString();
                            password = inforUser.get(1).toString();
                            User user = ServerCtr.CheckLogin(name, password);
                            Message newMessage = null;
                            if (user == null) {
                                newMessage = new Message(Message.Message_Type.LoginFailed);
                                ServerCtr.Send(TheClient, newMessage);
                                break;
                            }
                            newMessage = new Message(Message.Message_Type.LoginSucced);
                            ArrayList<Object> infor = new ArrayList<>();
                            infor.add(user);
                            infor.add(ServerCtr.getAllUsers());
                            newMessage.content = infor;
                            ServerCtr.Send(TheClient, newMessage);
                            TheClient.setName(name);
                            TheClient.setId(user.getId());
                            break;
                       
                        case CreateRoom:
                            ArrayList<String> informations = (ArrayList<String>) msg.content;
                            String roomName = informations.get(0);
                            String timeSetting = informations.get(1);
                            String side = informations.get(2);
                            int roomId = ServerCtr.IdRoom;

                            TheClient.setRoomName(roomName);
                            TheClient.setTimeSetting(timeSetting);
                            TheClient.setRoomId(roomId);
                            TheClient.setSide(side);

                            ++ServerCtr.IdRoom;
                            System.out.println(TheClient.getName() + " has create a room named : " + roomName);
                            break;
                        case ReturnRoomsNames:
                            Message newMsg = new Message(Message.Message_Type.ReturnRoomsNames);
                            newMsg.content = ServerCtr.ReturnRooms();
                            System.out.println("111");
                            ServerCtr.Send(TheClient, newMsg);
                            System.out.println("User " + TheClient.getName() + " refreshing rooms...");
                            break;
                        case JoinRoom:
                            if (ServerCtr.JoinRoom(msg.content.toString()) == null) {
                                System.out.println("Room is full");
                            } else {
                                Client opponent = ServerCtr.JoinRoom(msg.content.toString());

                                TheClient.setOpponent(opponent);
                                TheClient.getOpponent().setOpponent(TheClient);
                                TheClient.setRoomName(msg.content.toString());
                                TheClient.setRoomId(opponent.getRoomId());

                                Message joinMsgToRoomOwner = new Message(Message.Message_Type.JoinRoom);
                                joinMsgToRoomOwner.content = TheClient.getName();

                                Message joinMsgToOpponent = new Message(Message.Message_Type.JoinRoom);
                                ArrayList<String> informationsToOpponent = new ArrayList<>();
                                informationsToOpponent.add(TheClient.getName());
                                if (opponent.getSide().equals("white")) {
                                    TheClient.setSide("black");
                                } else {
                                    TheClient.setSide("white");
                                }
                                informationsToOpponent.add(TheClient.getSide());
                                informationsToOpponent.add(opponent.getTimeSetting());
                                informationsToOpponent.add(opponent.getName());
                                joinMsgToOpponent.content = informationsToOpponent;

                                ServerCtr.Send(TheClient.getOpponent(), joinMsgToRoomOwner);
                                ServerCtr.Send(TheClient, joinMsgToOpponent);

                                System.out.println("User " + TheClient.getName() + " is joining the room named " + TheClient.getRoomName());
                            }

                            break;
                        case MovePiece:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            System.out.println(TheClient.getName() + " has made a move...");
                            break;
                        case Attack:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            System.out.println(TheClient.getName() + " has made a attack...");
                            break;
                        case Upgrade:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            System.out.println(TheClient.getName() + " is upgrading his pawn");
                            break;
                        case EnemyLoss:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            UserDAO userDao = new UserDAO();
                            userDao.UpdateElo(TheClient.getOpponent().getId());
                            userDao.UpdateWin(TheClient.getOpponent().getId());
                            break;
                        case Ready:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            break;
                        case Start:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            break;
                        case ExitRoom:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            TheClient.ExitRoom();
                            break;
                        case Chat:
                            if (TheClient.getOpponent() == null)
                                break;
                            msg.content = TheClient.getName() + ": " + msg.content;
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            break;

                    }

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                ServerCtr.Clients.remove(TheClient);

            }
        }
    }
}