/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.clientModel.time;

import client.view.GameFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author X550V
 */
public class Timer extends Thread {

    private int ourTime, opponentTime;
    private GameFrame gameFrame;

    private boolean isRunning;

    private boolean ourTimerRunning;

    private JLabel clock1;

    private JLabel clock2;

    public Timer(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    
    public Timer(JLabel clock1, JLabel clock2) {
        this.clock1 = clock1;
        this.clock2 = clock2;
    }
    
    public void setTimeStart(String time) {
        ourTime = Integer.parseInt(time);
        opponentTime = Integer.parseInt(time);
    }

    @Override
    public void run() {
        System.out.println("Thread is running... Thread id is = " + this.getId());

        while (isRunning) {   // if its our turn we count down our time.
            try {
                if (ourTimerRunning) {
                    ourTime--;
                    gameFrame.setOurTime(String.valueOf(ourTime));
                } else {
                    opponentTime--;
                    gameFrame.setOppTime(String.valueOf(opponentTime));
                }
//                if(ourTime <= 0){
//                    Board.Game.CheckMateWTime();
//                }
                Thread.sleep(1000);  

            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("sea");
    }

    public void StartTimer(int t) {
        isRunning = true;
        this.start();
    }

    public synchronized void PauseTimer(int increaseTime) {
        ourTime += increaseTime;
        clock1.setText(String.valueOf(ourTime));
        ourTimerRunning = false;
    }

    public synchronized void PauseTimer() {
        ourTimerRunning = false;
    }

    public synchronized void ResumeTimer() {
        ourTimerRunning = true;
    }

    public void StopTimer() {
        isRunning = false;
    }

    public int GetTime() {
        return this.ourTime;
    }

    public boolean GetIsRunning() {
        return this.ourTimerRunning;
    }
}
