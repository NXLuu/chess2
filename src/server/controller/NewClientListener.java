/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import server.controller.ServerCtr;
import server.model.Client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;

/**
 *
 * @author X550V
 */
public class NewClientListener extends Thread {

    @Override
    public void run() {
        while (!ServerCtr.serverSocket.isClosed()) {
            try {
                Socket clientSocket = ServerCtr.serverSocket.accept();
                System.out.println("Client conntected");
                
                Client nclient = new Client(clientSocket, ServerCtr.IdClient);
                InetSocketAddress sockaddr = (InetSocketAddress)clientSocket.getRemoteSocketAddress();
                nclient.setIpConnect(sockaddr.getAddress().toString().split("/")[1]);
                nclient.getListenThread().start();
                nclient.getCaptureThread().setSendingPort(ServerCtr.portAudio);
                nclient.Send(new Message(Message.Message_Type.GetAudioPort, ServerCtr.portAudio));
                nclient.getCaptureThread().start();

                ServerCtr.portAudio++;
                ServerCtr.IdClient++;
                ServerCtr.Clients.add(nclient);

            } catch (IOException ex) {
                Logger.getLogger(NewClientListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
