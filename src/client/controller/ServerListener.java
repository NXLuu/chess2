/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Client;
import model.History;
import model.Message;
import model.ReqFr;
import model.User;
import server.controller.ServerCtr;

/**
 *
 * @author X550V
 */
public class ServerListener extends Thread {

    private GameController gameController;
    private ClientController clientController;
    private boolean running = true;

    public ServerListener() {
    }

    public ServerListener(GameController gameController, ClientController clientController) {
        this.gameController = gameController;
        this.clientController = clientController;
    }

    @Override
    public void run() {
        while (running && Client.socket.isConnected()) {
            try {
                Message received = (Message) (Client.sInput.readObject());

                switch (received.type) {
                    case ReturnRoomsNames:
                        Client.rooms = (ArrayList<String>) received.content;
                        gameController.refreshRooms(Client.rooms);
                        System.out.println("Reached rooms list from server...");
                        break;
                    case JoinRoom:
                        if (gameController.isRoomOwner()) {
                            gameController.getGameFrame().OpponentJoinedTheRoom(received.content.toString());
                            System.out.println("User " + received.content.toString() + " has joined the room...");
                        } else {
                            gameController.joinGame((ArrayList<String>) received.content);
                            System.out.println("You have joined the room");
                        }
//                        gameFrame.StartTimer();
                        break;
                    case MovePiece:
                        ArrayList readMoveInf = (ArrayList) received.content;
                        gameController.opponentMove((int) readMoveInf.get(0), (int) readMoveInf.get(1), (int) readMoveInf.get(2), (int) readMoveInf.get(3), (int) readMoveInf.get(4));
                        System.out.println("Opponent has made a move " + readMoveInf.get(0) + " is moving to " + (63 - (int) readMoveInf.get(1)));
                        break;
                    case EnemyLoss:
                        gameController.finishGame(true);
                    case Ready:
                        gameController.getGameFrame().enemyReady();
                        break;
                    case Start:
                        gameController.startGame();
                        break;
                    case ExitRoom:
                        gameController.enemyExit();
                        gameController.getGameFrame().enemyExitRoom();
                        gameController.getGameFrame().isRoomOwner = true;
                        break;
                    case LoginSucced:
                        clientController.showMessage("Login Succed");
                        ArrayList<Object> infor = (ArrayList<Object>) received.content;
                        User user = (User) infor.get(0);
                        ArrayList<User> userList = (ArrayList<User>) infor.get(1);
                        clientController.startGameClient(user);
                        gameController.refreshUsers(userList);
                        break;
                    case LoginFailed:
                        clientController.showMessage("Invalid username or passowrd");
                        break;
                    case Chat:
                        gameController.setTextGameFrame((String) received.content, false);
                        break;
                    case RefreshHis:
                        gameController.refreshHis((List<History>) received.content);
                        break;
                    case SearchUser:
                        List<User> userSearch = (ArrayList<User>) received.content;
                        gameController.setUserSearch(userSearch);
                        break;
                    case SendFriendReq:
                        gameController.reloadFrReq();
                        break;
                    case ReloadFriendReq:
                        List<ReqFr> reqList = (ArrayList<ReqFr>) received.content;
                        gameController.setFrReq(reqList);
                        break;
                    case GetFriendList:
                        List<User> users = (ArrayList<User>) received.content;
                        gameController.setFrs(users);
                        break;
                    case Disconnect:
                        running = false;
                        System.exit(0);
                    case GetAudioPort:
                        int port = (int)received.content;
                        Client.audioCtr.setSendingPort(port);
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
