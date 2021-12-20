/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import client.controller.AudioController;
import client.controller.ClientController;
import client.controller.GameController;
import client.controller.ServerListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author X550V
 */
public class Client {

    public static Socket socket;
    public static ObjectInputStream sInput;
    public static ObjectOutputStream sOutput;
    public static ServerListener listenMe;
    public static ArrayList<String> rooms;
    public static boolean isLogin = false;
    public static AudioController audioCtr;

    public static void StartAudio(String ip, int port1, int port2) {

    }

    public static void Start(String ip, int port, GameController gameController, ClientController clientController) {
        try {
            Client.socket = new Socket(ip, port);
            audioCtr = new AudioController(ip, 1652, 59999);
            audioCtr.captureAudio();
            Client.Display("Server success connection");
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenMe = new ServerListener(gameController, clientController);
            Client.listenMe.start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void StartWithLogin(String ip, int port, String userName, String password) {
        try {
            Client.socket = new Socket(ip, port);
            Client.Display("Server success connection");
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenMe = new ServerListener();
            Client.listenMe.start();

            Message msg = new Message(Message.Message_Type.Login);
            ArrayList inf = new ArrayList();
            inf.add(userName);
            inf.add(password);
            msg.content = inf;
            Client.Send(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.sOutput.flush();
                Client.sOutput.close();
                Client.sInput.close();
                Client.socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void StopRunning() {
        Client.listenMe.setRunning(false);
    }

    public static void Display(String msg) {

        System.out.println(msg);

    }

    //mesaj gönderme fonksiyonu
    public static void Send(Message msg) {
        try {
            if (Client.socket.isConnected()) {
                Client.sOutput.writeObject(msg);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
