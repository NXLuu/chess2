/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;
import server.controller.CaptureThread;
import server.controller.ClientListener;
import server.controller.ServerCtr;

/**
 *
 * @author X550V
 */
public class Client {

    int id;
    int roomId = -1;
    String name = "NoName";
    String roomName = "Null";
    String timeSetting = "Null";
    String side = "Null";
    Socket soket;
    private String ipConnect = "";
    ObjectOutputStream sOutput;
    ObjectInputStream sInput;
    ClientListener listenThread;
    CaptureThread captureThread;
    Client opponent;
    boolean isLogin = false;

    public String getIpConnect() {
        return ipConnect;
    }

    public void setIpConnect(String ipConnect) {
        this.ipConnect = ipConnect;
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public CaptureThread getCaptureThread() {
        return captureThread;
    }

    public void setCaptureThread(CaptureThread captureThread) {
        this.captureThread = captureThread;
    }

    public Client(Socket gelenSoket, int id) {
        this.soket = gelenSoket;
        this.id = id;
        try {
            this.sOutput = new ObjectOutputStream(this.soket.getOutputStream());
            this.sInput = new ObjectInputStream(this.soket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.listenThread = new ClientListener(this);
        this.captureThread = new CaptureThread();
    }

    public void Send(Message message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ExitRoom() {
        roomName = "Null";
        timeSetting = "Null";
        side = "Null";
        roomId = -1;
        if (opponent != null) {
            opponent.opponent = null;
        }
        captureThread.setIpConnect(null);
        this.opponent = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTimeSetting() {
        return timeSetting;
    }

    public void setTimeSetting(String timeSetting) {
        this.timeSetting = timeSetting;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Socket getSoket() {
        return soket;
    }

    public void setSoket(Socket soket) {
        this.soket = soket;
    }

    public ObjectOutputStream getsOutput() {
        return sOutput;
    }

    public void setsOutput(ObjectOutputStream sOutput) {
        this.sOutput = sOutput;
    }

    public ObjectInputStream getsInput() {
        return sInput;
    }

    public void setsInput(ObjectInputStream sInput) {
        this.sInput = sInput;
    }

    public ClientListener getListenThread() {
        return listenThread;
    }

    public void setListenThread(ClientListener listenThread) {
        this.listenThread = listenThread;
    }

    public Client getOpponent() {
        return opponent;
    }

    public void setOpponent(Client opponent) {
        this.opponent = opponent;
    }

}
