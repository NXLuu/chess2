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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.History;
import model.Message;
import model.ReqFr;
import model.User;
import server.logicApplication.UserDAO;

/**
 *
 * @author X550V
 */
public class ClientListener extends Thread {

    private Client TheClient;

    public ClientListener(Client TheClient) {
        this.TheClient = TheClient;
    }

    @Override
    public void run() {
        while (!TheClient.getSoket().isClosed()) {
            try {
                try {
                    Message msg;
                    String name;
                    String password;

                    if (!TheClient.getSoket().isClosed()) {
                        msg = (Message) TheClient.getsInput().readObject();
                    } else {
                        msg = new Message(Message.Message_Type.Disconnect);
                    }
                    Message sendMsg = new Message(Message.Message_Type.Text);

                    UserDAO dao = new UserDAO();
                    switch (msg.type) {
                        case JoinServer:
                            name = msg.content.toString();
                            TheClient.setName(name);
                            ServerCtr.Send(TheClient, new Message(Message.Message_Type.ReturnRoomsNames, ServerCtr.ReturnRooms()));
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
                            TheClient.setIsLogin(true);
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

                            ServerCtr.sendBroadCast(new Message(Message.Message_Type.ReturnRoomsNames, ServerCtr.ReturnRooms()));
                            break;
                        case ReturnRoomsNames:
                            Message newMsg = new Message(Message.Message_Type.ReturnRoomsNames);
                            newMsg.content = ServerCtr.ReturnRooms();
                            ServerCtr.Send(TheClient, newMsg);
                            System.out.println("User " + TheClient.getName() + " refreshing rooms...");
                            break;
                        case JoinRoom:
                            if (ServerCtr.JoinRoom(msg.content.toString()) == null) {
                                System.out.println("Room is full");
                            } else {
                                Client opponent = ServerCtr.JoinRoom(msg.content.toString());
                                opponent.getCaptureThread().setIpConnect(TheClient.getIpConnect());
                                TheClient.setOpponent(opponent);
                                TheClient.getCaptureThread().setIpConnect(opponent.getIpConnect());
                                TheClient.getOpponent().setOpponent(TheClient);
                                TheClient.setRoomName(msg.content.toString());
                                TheClient.setRoomId(opponent.getRoomId());
                                TheClient.setTimeSetting(opponent.getTimeSetting());

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

                                ServerCtr.sendBroadCast(new Message(Message.Message_Type.ReturnRoomsNames, ServerCtr.ReturnRooms()));
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
                            if (TheClient.getOpponent().isIsLogin()) {
                                userDao.UpdateElo(TheClient.getOpponent().getId());
                                userDao.UpdateWin(TheClient.getOpponent().getId());
                                userDao.insertHistery(TheClient.getOpponent().getId(), TheClient.getName(), true);
                            }

                            if (TheClient.isIsLogin()) {
                                userDao.insertHistery(TheClient.getId(), TheClient.getOpponent().getName(), false);
                            }

                            break;
                        case Ready:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            break;
                        case Start:
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            break;
                        case ExitRoom:
                            if (TheClient.getOpponent() != null) {
                                ServerCtr.Send(TheClient.getOpponent(), msg);
                            }
                            TheClient.ExitRoom();

                            ServerCtr.sendBroadCast(new Message(Message.Message_Type.ReturnRoomsNames, ServerCtr.ReturnRooms()));
                            break;
                        case Chat:
                            if (TheClient.getOpponent() == null) {
                                break;
                            }
                            msg.content = TheClient.getName() + ": " + msg.content;
                            ServerCtr.Send(TheClient.getOpponent(), msg);
                            break;
                        case Sigup:
                            User newUser = (User) msg.content;
                            name = newUser.getUserName();
                            password = newUser.getPassword();
                            ServerCtr.Sigup(name, password);
                            break;
                        case RefreshHis:
                            List<History> his = ServerCtr.getAllHis(TheClient.getId());
                            Message hisMsg = new Message(Message.Message_Type.RefreshHis);
                            hisMsg.content = his;
                            ServerCtr.Send(TheClient, hisMsg);
                            break;
                        case GetFriendReq:
                            if (!TheClient.isIsLogin()) {
                                break;
                            }
                            List<ReqFr> req = ServerCtr.getAllFriendReq(TheClient.getId());
                            Message msgFr = new Message(Message.Message_Type.GetFriendReq);
                            msgFr.content = req;
                            ServerCtr.Send(TheClient, msgFr);
                            break;
                        case SendFriendReq:
                            if (!TheClient.isIsLogin()) {
                                break;
                            }
                            int idSent = (int) msg.content;
                            dao.createFrReq(idSent, TheClient.getId());
                            Message msgReq = new Message(Message.Message_Type.SendFriendReq);
                            Client c = ServerCtr.getClientById(idSent);
                            if (c != null) {
                                ServerCtr.Send(c, msgReq);
                            }

                            sendMsg.type = Message.Message_Type.ReloadFriendReq;
                            sendMsg.content = dao.getFriendReq(idSent);
                            if (ServerCtr.getClientById(idSent) != null) {
                                ServerCtr.Send(ServerCtr.getClientById(idSent), sendMsg);
                            }
                            break;
                        case ReloadFriendReq:
                            sendMsg.type = Message.Message_Type.ReloadFriendReq;
                            sendMsg.content = dao.getFriendReq(TheClient.getId());
                            ServerCtr.Send(TheClient, sendMsg);
                            break;
                        case AcceptReq:
                            int idReq = (int) msg.content;
                            ReqFr reqFr = dao.getFriendReqById(idReq);

                            dao.createFriendShip(reqFr.getIdSend(), reqFr.getIdRev());
                            dao.createFriendShip(reqFr.getIdRev(), reqFr.getIdSend());
                            dao.deleteFr(idReq);

                            sendMsg.type = Message.Message_Type.GetFriendList;
                            sendMsg.content = dao.getFr(TheClient.getId());
                            ServerCtr.Send(TheClient, sendMsg);

                            sendMsg.type = Message.Message_Type.GetFriendList;
                            sendMsg.content = dao.getFr(reqFr.getIdSend());
                            if (ServerCtr.getClientById(reqFr.getIdSend()) != null) {
                                ServerCtr.Send(ServerCtr.getClientById(reqFr.getIdSend()), sendMsg);
                            }
                            break;
                        case GetFriendList:
                            sendMsg.type = Message.Message_Type.GetFriendList;
                            sendMsg.content = dao.getFr(TheClient.getId());
                            ServerCtr.Send(TheClient, sendMsg);
                            break;
                        case SearchUser:
                            name = msg.content.toString();
                            List<User> userList = dao.search(name);
                            List<User> frList = dao.getFr(TheClient.getId());
                            List<ReqFr> frs = dao.getFriendReq(TheClient.getId());
                            Iterator itr = userList.iterator();
                            while (itr.hasNext()) {
                                User x = (User) itr.next();
                                if (x.getId() == TheClient.getId()) {
                                    itr.remove();
                                    break;
                                }
                                for (User fr : frList) {
                                    if (x.getId() == fr.getId()) {
                                        itr.remove();
                                        break;
                                    }
                                }

                                for (ReqFr fr1 : frs) {
                                    if (x.getId() == fr1.getIdRev()) {
                                        itr.remove();
                                        break;
                                    }
                                }
                            }
                            Message msgUser = new Message(Message.Message_Type.SearchUser);
                            msgUser.content = userList;
                            ServerCtr.Send(TheClient, msgUser);
                            break;
                        case Disconnect:
                            ServerCtr.Send(TheClient, new Message(Message.Message_Type.Disconnect));
                            TheClient.getSoket().close();
                            ServerCtr.removeClient(TheClient);

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
        ServerCtr.removeClient(TheClient);
    }
}
