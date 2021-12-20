/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author nxulu
 */
public class CaptureThread extends Thread {

    byte tempBuffer[] = new byte[1024];
    boolean running = true;
    private String ipConnect;
    private int sendingPort;
 

    public String getIpConnect() {
        return ipConnect;
    }

    public void setIpConnect(String ipConnect) {
        this.ipConnect = ipConnect;
    }

    public int getSendingPort() {
        return sendingPort;
    }

    public void setSendingPort(int sendingPort) {
        this.sendingPort = sendingPort;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        try {
            DatagramSocket server_socket = new DatagramSocket(sendingPort);
            while (running) {
                DatagramPacket receive_packet = new DatagramPacket(tempBuffer,
                        tempBuffer.length);
                server_socket.receive(receive_packet);

                if (ipConnect != null) {
                    DatagramSocket client_socket = new DatagramSocket();
                    InetAddress IPAddress = InetAddress.getByName(ipConnect);
                    receive_packet.setAddress(IPAddress);
                    receive_packet.setPort(59999);
                    client_socket.send(receive_packet);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
