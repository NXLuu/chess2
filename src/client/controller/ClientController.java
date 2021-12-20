/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.clientModel.game.Game;
import client.view.GameClient;
import client.view.GetNameJLog;
import client.view.LoginJLog;
import client.view.Menu;
import client.view.SignUpJlog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Client;
import model.Message;
import model.User;

/**
 *
 * @author nxulu
 */
public class ClientController {

    private GameController gameController;
    private Menu menu;
    GetNameJLog getnameLog;
    private String serverHost = "localhost";
    private int serverPort = 8124;

    public ClientController() {
        menu = new Menu();
        menu.setVisible(true);

        gameController = new GameController();

        menu.addLoginAction(new LoginListener());
        menu.addGuessAction(new GuessListener());
        menu.addconnectAction(new ConnectListener());
        menu.addRegisterAction(new RegisterListener());
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(menu, message, "Error", JOptionPane.OK_OPTION);
    }

    public void connetToServer() {
        Client.Start(serverHost, serverPort, gameController, this);
    }

    class ConnectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            connetToServer();
        }

    }

    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            LoginJLog loginLog = new LoginJLog(menu, true);
            loginLog.getjButton1().addActionListener(er -> login(loginLog));
            loginLog.setVisible(true);
        }

    }

    class RegisterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SignUpJlog sigupLog = new SignUpJlog(menu, true);
            sigupLog.getjButton1().addActionListener(er -> signUp(sigupLog));
            sigupLog.setVisible(true);
        }

    }

    class GuessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GetNameJLog getnameLog = new GetNameJLog(menu, true);
            getnameLog.getjButton1().addActionListener(er -> getName(getnameLog));
            getnameLog.setVisible(true);
        }

    }

    // Dialoag Listener
    public void login(LoginJLog loginLog) {
        User user = loginLog.getUser();
        Message msg = new Message(Message.Message_Type.Login);
        ArrayList<String> infor = new ArrayList<>();
        infor.add(user.getUserName());
        infor.add(user.getPassword());
        msg.content = infor;
        Client.Send(msg);
        loginLog.dispose();
    }

    public void startGameClient(User user) {
        gameController.initGameClient(user);
        gameController.showGameClient();
        menu.setVisible(false);
    }

    public void getName(GetNameJLog getnameLog) {
        String userName = getnameLog.getUserName();
        Message msg = new Message(Message.Message_Type.JoinServer);
        msg.content = userName;
        User user = new User();
        user.setUserName(userName);
        startGameClient(user);
        Client.Send(msg);
        getnameLog.dispose();
    }

    public void signUp(SignUpJlog sigupLog) {
        User user = sigupLog.getUser();
        Message msg = new Message(Message.Message_Type.Sigup);
        msg.content = user;
        Client.Send(msg);
        sigupLog.dispose();
    }

    class GetName implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = getnameLog.getUserName();
            Message msg = new Message(Message.Message_Type.JoinServer);
            msg.content = userName;
            Client.Send(msg);
            getnameLog.dispose();
        }
    }
}
