/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import model.Message;
import java.io.IOException;

/**
 *
 * @author nxulu
 */
public class GameFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = -4442947819954124379L;
    public static final int WIDTH = 640;
    public static final int HEIGTH = 640;
//    public GamePanel gamePanel;
    public String side;
    public String timeSettings;
    public String userName;
    public String opponentUserName;
    public boolean isRoomOwner = false;
    public boolean enemyReady = false;
    public static GameFrame gameFrame;
    private int yTextPos = 20;
    private boolean mic = false;
    private boolean speaker = false;

    public boolean isMic() {
        return mic;
    }

    public void setMic(boolean mic) {
        this.mic = mic;
    }

    public boolean isSpeaker() {
        return speaker;
    }

    public void setSpeaker(boolean speaker) {
        this.speaker = speaker;
    }

    public String getTimeSettings() {
        return timeSettings;
    }

    public void setTimeSettings(String timeSettings) {
        this.timeSettings = timeSettings;
    }

    public void setOurTime(String time) {
        jLabel3.setText(time);
    }

    public void setOppTime(String time) {
        jLabel4.setText(time);
    }

    public void enemyLoss(Message mess) {
//        reset();
    }

    public void resetTime() {
        jLabel3.setText(timeSettings);
        jLabel4.setText(timeSettings);
    }

    public String getChatMess() {
        return jTextField1.getText();
    }

    public void setChatArea(String mess, boolean isMe) {
        String str = "<html>" + mess
                + "</html>";
        JTextArea jtext = new JTextArea(mess);

        JTextPane txpPane = new JTextPane();
        JTextPane output = new JTextPane();

        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
        output.setParagraphAttributes(attribs, true);
        output.setText(str);
        JLabel jLabel = new JLabel(str);
        jLabel.setSize(new Dimension(250, 250));
        jLabel.setHorizontalAlignment(JLabel.LEFT);
        output.setSize(new Dimension(300, 100));
        jLabel1.setBackground(Color.red);
        jtext.setSize(250, 100);
        jtext.setWrapStyleWord(true);
        jtext.setLineWrap(true);
        jtext.setEditable(false);
        jtext.setFont(new java.awt.Font("Adobe Fan Heiti Std B", 1, 12)); // NOI18N
        int xPos = 0;
        if (isMe) {
            xPos = 20;
            jLabel.setHorizontalAlignment(JLabel.RIGHT);
            jtext.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jtext.setText(mess);
        }

        jPanel4.add(jtext, new org.netbeans.lib.awtextra.AbsoluteConstraints(xPos, yTextPos, 250, 40));
        yTextPos += 40;
    }

    public void setButton() {
        if (isRoomOwner) {
            jButton2.setText("Start");
            if (!enemyReady) {
                jButton2.setEnabled(false);
            }
        } else {
            jButton2.setText("Ready");
        }
    }

    public void restartGame() {
        setButton();
        jPanel6.setVisible(false);
        resetTime();
    }

    public void showFinishMessage(boolean isWin) {
//        reset();
        String text = "YOU WIN !!!";
        if (!isWin) {
            text = "YOU LOSS !!!";
        }
        jLabel6.setText(text);
        jPanel6.setVisible(true);
    }

    public void addGamePanel(GamePanel gamePanel) {
        jPanel1.add(gamePanel);
    }

    public GameFrame(String side, String timeSettings, String userName, String opponentUserName, boolean isOwnerRoom) {

        initComponents();
        jLabel2.setText(userName);
        jLabel1.setText(opponentUserName);
        boolean isWhite = ("white".equals(side)) ? true : false;
        this.side = side;
        this.timeSettings = timeSettings;
        this.userName = userName;
        this.opponentUserName = opponentUserName;
        this.isRoomOwner = isOwnerRoom;
        setButton();
        jPanel6.setVisible(false);
        gameFrame = this;
        resetTime();
    }

    public void setMyName(String name) {
        jLabel2.setText(name);
    }

    public void enemyReady() {
        jLabel5.setText("Ready");
        jButton2.setEnabled(true);
        enemyReady = true;

    }

    public void enemyExitRoom() {
        jLabel1.setText("Enemy");
        jLabel5.setText("Waiting...");
        if (!isRoomOwner) {
            isRoomOwner = true;
            jButton2.setText("Start");
        }
//        if (gamePanel.game.gameIsStarted) {
//            gamePanel.game.gameIsStarted = false;
//        }
    }

    public void startGame() {
//        gamePanel.game.gameIsStarted = true;
    }

    public void OpponentJoinedTheRoom(String name) {
        jLabel1.setText(name);

    }

    /**
     * Creates new form GameFrame
     */
    public GameFrame(boolean side) {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        gameFrame = this;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Player 1");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Player 2");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("0");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("0");

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Start");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Watting...");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Exit Room");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel5.setMinimumSize(new java.awt.Dimension(226, 166));
        jPanel5.setName(""); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        jLabel6.setText("You Win !!!");

        jButton4.setText("Restart");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(jButton4))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        try {
            BufferedImage img1 = ImageIO.read(getClass().getResource("/client/clientModel/asset/mute-mic.png"));
            jButton5.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(img1).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        } catch (IOException e) {
        }
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        try {
            BufferedImage img2 = ImageIO.read(getClass().getResource("/client/clientModel/asset/mute-speaker.png"));
            jButton6.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(img2).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        } catch (IOException e) {
        }
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(59, 59, 59)
                                        .addComponent(jLabel2)
                                        .addGap(51, 51, 51))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(67, 67, 67)))
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(jButton3))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(62, 62, 62)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(212, 212, 212)
                                .addComponent(jButton5)
                                .addGap(36, 36, 36)
                                .addComponent(jButton6)))
                        .addGap(0, 46, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(14, 14, 14))
        );

        jButton1.setText("Send");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane2.setViewportView(jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane2)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void addListener(ActionListener startListener, ActionListener exitListener, ActionListener restartListener, ActionListener chatListener, ActionListener mic, ActionListener speaker) {
        jButton2.addActionListener(startListener);
        jButton3.addActionListener(exitListener);
        jButton4.addActionListener(restartListener);
        jButton1.addActionListener(chatListener);
        jButton5.addActionListener(mic);
        jButton6.addActionListener(speaker);

    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton2ActionPerformed

    public void exit() {

    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        exit();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (mic) {
            try {
                BufferedImage img1 = ImageIO.read(getClass().getResource("/client/clientModel/asset/mute-mic.png"));
                jButton5.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(img1).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            } catch (IOException e) {
            }
        } else {
            try {
                BufferedImage img1 = ImageIO.read(getClass().getResource("/client/clientModel/asset/mic.png"));
                jButton5.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(img1).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            } catch (IOException e) {
            }
        }
        mic = !mic;
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
//        jButton6.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon("src/client/clientModel/asset/speaker.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        if (speaker) {
            try {
                BufferedImage img2 = ImageIO.read(getClass().getResource("/client/clientModel/asset/mute-speaker.png"));
                jButton6.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(img2).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            } catch (IOException e) {
            }
        } else {
            try {
                BufferedImage img2 = ImageIO.read(getClass().getResource("/client/clientModel/asset/speaker.png"));
                jButton6.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(img2).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            } catch (IOException e) {
            }
        }
        speaker = !speaker;
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
