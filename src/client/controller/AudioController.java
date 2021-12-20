/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author nxulu
 */
public class AudioController {

    private String ipConnect;
    private int sendingPort;
    private int receivingPort;
    private PlayThread playThread;
    private CaptureThread captureThread;

    DatagramSocket server_socket;
    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    SourceDataLine sourceDataLine;
    AudioInputStream audioInputStream;

    public AudioController(String ipConnect, int sendingPort, int receivingPort) {
        this.ipConnect = ipConnect;
        this.sendingPort = sendingPort;
        this.receivingPort = receivingPort;
    }

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

    public int getReceivingPort() {
        return receivingPort;
    }

    public void setReceivingPort(int receivingPort) {
        this.receivingPort = receivingPort;
    }

    public void captureAudio() {
        try {
            //print audio devices informatioin
            Mixer.Info[] mixerInfo
                    = AudioSystem.getMixerInfo();
            System.out.println("Available mixers:");
            for (Mixer.Info mixerInfo1 : mixerInfo) {
                System.out.println(mixerInfo1.getName());
            }
            // get audio from mic X
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

            Mixer mixer = AudioSystem.getMixer(mixerInfo[5]);
            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            // call thread to send audio

            // send audio to speaker X
            DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            // call thread to recive audio

        } catch (LineUnavailableException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void startCapture() {
        playThread = new PlayThread();
        playThread.start();
    }

    public void startSendVoice() {
        captureThread = new CaptureThread();
        captureThread.start();
    }

    public void stopCapture() {
        playThread.setRunning(false);
    }

    public void stopSendVoid() {
        captureThread.setRunning(false);
    }

    // sending audio to server
    class CaptureThread extends Thread {

        byte tempBuffer[] = new byte[1024];
        boolean running = true;

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            try {
                DatagramSocket client_socket = new DatagramSocket();
                while (running) {
                    InetAddress IPAddress = InetAddress.getByName(ipConnect);
                    int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    DatagramPacket send_packet = new DatagramPacket(tempBuffer, tempBuffer.length,
                            IPAddress, sendingPort);
                    client_socket.send(send_packet);
                }
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }
// coding and format audio

    private AudioFormat getAudioFormat() {
        float sampleRate = 14000.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate,
                sampleSizeInBits, channels, signed, bigEndian);
    }
// recieving audio from server and play it

    class PlayThread extends Thread {

        byte tempBuffer[] = new byte[1024];
        boolean running = true;

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            try {
                DatagramSocket server_socket = new DatagramSocket(receivingPort);
                while (running) {
                    DatagramPacket receive_packet = new DatagramPacket(tempBuffer,
                            tempBuffer.length);
                    server_socket.receive(receive_packet);
                    sourceDataLine.write(receive_packet.getData(), 0, tempBuffer.length);
                }
            } catch (IOException e) {
            }
        }
    }
}
