/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.view.GameFrame;
import client.view.GamePanel;
import client.clientModel.game.Game;
import client.clientModel.pieces.Piece;
import client.clientModel.time.Timer;
import client.view.GameClient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.Client;
import model.History;
import model.Message;
import model.ReqFr;
import model.User;

/**
 *
 * @author nxulu
 */
public class GameController {

    private GameFrame gameFrame;
    private GamePanel gamePanel;
    private Game game;
    private GameClient gameclient;
    private Timer timer;

    public GameController() {
    }

    public GameController(GameFrame gameFrame, GamePanel gamePanel, Game game, GameClient gameclient) {
        this.gameFrame = gameFrame;
        this.gamePanel = gamePanel;
        this.game = game;
        this.gameclient = gameclient;

    }

    public void opponentMove(int fromX, int fromY, int x, int y, int choicePromte) {
        game.opponentMove(fromX, fromY, x, y, choicePromte);
        timer.ResumeTimer();
        if (game.opponentCheckMate()) {
            finishGame(false);
        }
    }

    public void timeOut() {
        game.sendLossInfor();
        finishGame(false);
    }

    public void enemyExit() {
        gameFrame.enemyExitRoom();
        gameFrame.enemyReady = false;
        timer.StopTimer();
        gameFrame.resetTime();
        if (game.gameIsStarted) {
            finishGame(true);
        }
        game.gameIsStarted = false;

    }

    public void initGamePanel() {
        gamePanel.addMouseListener(new MouseGamePanel());
        gamePanel.addMouseMotionListener(new MouseGamePanel());
    }

    public void initGameClient(User user) {
        gameclient = new GameClient(user);
        gameclient.addResetFr(new ResetFrListener());
        gameclient.addAccFrReq(new AcceptFrReqListener());
        gameclient.addResetFrReq(new ResetFrReqListener());
        gameclient.addSearch(new SearchUserListener());
        gameclient.addSendFrReq(new SendFriendReq());
        gameclient.addCreateRoomLis(new CreateRoomListener());
        gameclient.addjoinRoomLis(new JoinRoomListener());
        gameclient.addRefreshRoomLis(new RefreshRoomListener());
        gameclient.addRefreshHis(new RefreshHisListener());
        gameclient.setDefaultCloseOperation(GameClient.DO_NOTHING_ON_CLOSE);
        gameclient.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(gameclient,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    Client.Send(new Message(Message.Message_Type.Disconnect));
                }
            }
        });
        gameclient.setVisible(true);
    }

    public void showGameClient() {
        gameclient.setVisible(true);
    }

    public void initGameFrame(String side, String timeSetting, String userName, String opponentName, boolean isRoomOwner) {
        gameFrame = new GameFrame(side, timeSetting, userName, opponentName, isRoomOwner);
        gameFrame.addListener(new StartListener(), new ExitListner(), new RestartGame(), new ChatListener(), new MicListener(), new SpeakerListener());
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exit();
            }
        });
    }

    // handler clientGame
    public boolean isRoomOwner() {

        return gameFrame != null && gameFrame.isRoomOwner;
    }

    // Handler game
    public void startGame() {
        game.gameIsStarted = true;
        timer = new Timer(this, Integer.parseInt(gameFrame.getTimeSettings()));
        timer.setTimeStart(gameFrame.getTimeSettings());
        if (game.checkMyTurn()) {
            timer.ResumeTimer();
        }
        timer.StartTimer(0);
    }

    public void initGame(boolean side) {

        game = new Game(side, this);
        gamePanel = new GamePanel(side, game);
        initGamePanel();
        gamePanel.setVisible(true);
        gameFrame.addGamePanel(gamePanel);
        gameFrame.setVisible(true);
        timer = new Timer(this, Integer.parseInt(gameFrame.getTimeSettings()));
        game.start();
    }

    public void finishGame(boolean isWin) {
        gameFrame.showFinishMessage(isWin);
        timer.StopTimer();
        game.gameIsStarted = false;
    }

    public void joinGame(ArrayList<String> informations) {
        initGameFrame(informations.get(1), informations.get(2), informations.get(0), informations.get(3), false);
        boolean side = "white".equals(informations.get(1));
        initGame(side);
        gameFrame.setVisible(true);
    }

    public void closeGame() {
        gameFrame.dispose();
        gameFrame = null;
    }

    public void resetGame() {
        game.close();
    }

    public void reloadFrReq() {

    }

    public void refreshRooms(ArrayList<String> rooms) {
        gameclient.setListRoom(rooms);
    }

    public void setUserSearch(List<User> users) {
        gameclient.setUserSearch(users);
    }

    public void setFrs(List<User> users) {
        gameclient.setFrList(users);
    }

    public void setFrReq(List<ReqFr> reqs) {
        gameclient.setFrReq(reqs);
    }

    public void refreshUsers(ArrayList<User> userList) {
        gameclient.setListUsers(userList);
    }

    public void refreshHis(List<History> hisList) {
        gameclient.setHist(hisList);
    }

    // Action listener GameClient
    class CreateRoomListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = gameclient.getInforRoom();
            Client.Send(msg);

            ArrayList<String> roomInfor = (ArrayList<String>) msg.content;

            initGameFrame(roomInfor.get(2), roomInfor.get(1), gameclient.getUserName(), "", true);
            gameFrame.isRoomOwner = true;
            boolean side = "white".equals(roomInfor.get(2));
            initGame(side);
        }
    }

    class ResetFrListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = new Message(Message.Message_Type.GetFriendList);
            Client.Send(msg);
        }
    }

    class AcceptFrReqListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = new Message(Message.Message_Type.AcceptReq);
            msg.content = gameclient.getSelectedFrReq();
            Client.Send(msg);
        }
    }

    class ResetFrReqListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = new Message(Message.Message_Type.ReloadFriendReq);
            Client.Send(msg);
        }
    }

    class SearchUserListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = new Message(Message.Message_Type.SearchUser);
            msg.content = gameclient.getSearchName();
            Client.Send(msg);
        }
    }

    class SendFriendReq implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String idStr = gameclient.getSelectUser();
            if (idStr == null) {
                return;
            }
            int id = Integer.parseInt(idStr);
            Message msg = new Message(Message.Message_Type.SendFriendReq);
            msg.content = id;
            Client.Send(msg);
        }
    }

    class RefreshRoomListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameclient.clearList();
            Message msg = new Message(Message.Message_Type.ReturnRoomsNames);
            Client.Send(msg);
        }
    }

    class RefreshHisListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = new Message(Message.Message_Type.RefreshHis);
            Client.Send(msg);
        }
    }

    class JoinRoomListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = new Message(Message.Message_Type.JoinRoom);
            msg.content = gameclient.getSelectedList();
            Client.Send(msg);
        }
    }

    // Action listener GameFrame
    public void setTextGameFrame(String mess, boolean isMe) {
        gameFrame.setChatArea(mess, isMe);
    }

    class SpeakerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameFrame.isSpeaker()) {
                Client.audioCtr.startCapture();
            } else {
                Client.audioCtr.stopCapture();
            }

        }

    }

    class MicListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameFrame.isMic()) {
                Client.audioCtr.startSendVoice();
            } else {
                Client.audioCtr.stopSendVoid();
            }

        }

    }

    class ChatListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String mess = gameFrame.getChatMess();
            gameFrame.setChatArea(mess, true);
            Message msg = new Message(Message.Message_Type.Chat);
            msg.content = mess;
            Client.Send(msg);
        }

    }

    class StartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameFrame.isRoomOwner) {
                if (!gameFrame.enemyReady) {
                    return;
                }
                Client.Send(new Message(Message.Message_Type.Start));
                startGame();
            } else {
                Message msg = new Message(Message.Message_Type.Ready);
                Client.Send(msg);
            }
        }

    }

    public void exit() {
        Client.Send(new Message(Message.Message_Type.ExitRoom));
        gameFrame.isRoomOwner = false;
        gameFrame.resetTime();
        resetGame();
        closeGame();
    }

    class ExitListner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            exit();

        }

    }

    class RestartGame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            gameFrame.restartGame();
            game.reset();
        }
    }

    // Action listener
    class MouseGamePanel extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                int x = e.getX() / Piece.size;
                int y = e.getY() / Piece.size;
                game.drag = false;
                game.active = null;
                game.selectPiece(x, y);
                gamePanel.revalidate();
                gamePanel.repaint();
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int x = e.getX() / Piece.size;
            int y = e.getY() / Piece.size;

            if (game.move(x, y)) {
                timer.PauseTimer();
            }
            gamePanel.revalidate();
            gamePanel.repaint();
        }
    }

    //getter setter
    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameClient getGameclient() {
        return gameclient;
    }

    public void setGameclient(GameClient gameclient) {
        this.gameclient = gameclient;
    }
}
