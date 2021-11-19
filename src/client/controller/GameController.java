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
import javax.swing.SwingUtilities;
import model.Client;
import model.Message;
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

    public void initGamePanel() {
        gamePanel.addMouseListener(new MouseGamePanel());
        gamePanel.addMouseMotionListener(new MouseGamePanel());
    }

    public void initGameClient(User user) {
        gameclient = new GameClient(user);
        gameclient.addCreateRoomLis(new CreateRoomListener());
        gameclient.addjoinRoomLis(new JoinRoomListener());
        gameclient.addRefreshRoomLis(new RefreshRoomListener());
        gameclient.setVisible(true);
    }

    public void showGameClient() {
        gameclient.setVisible(true);
    }

    public void initGameFrame(String side, String timeSetting, String userName, String opponentName, boolean isRoomOwner) {
        gameFrame = new GameFrame(side, timeSetting, userName, opponentName, isRoomOwner);
        gameFrame.addListener(new StartListener(), new ExitListner(), new RestartGame(), new ChatListener());
    }

    // handler clientGame
    public boolean isRoomOwner() {

        return gameFrame != null && gameFrame.isRoomOwner;
    }

    // Handler game
    public void startGame() {
        game.gameIsStarted = true;
        timer = new Timer(gameFrame);
        timer.setTimeStart(gameFrame.getTimeSettings());
        if (gameFrame.isRoomOwner) {
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

        game.start();
    }

    public void finishGame(boolean isWin) {
        gameFrame.showFinishMessage(isWin);
        game.gameIsStarted = false;
    }

    public void joinGame(ArrayList<String> informations) {
        initGameFrame(informations.get(1), informations.get(2), informations.get(0), informations.get(3), false);
        boolean side = informations.get(0) == "white";
        initGame(side);
        gameFrame.setVisible(true);
    }

    public void closeGame() {
        gameFrame.dispose();
    }

    public void resetGame() {
        game.reset();
    }

    public void refreshRooms(ArrayList<String> rooms) {
        gameclient.setListRoom(rooms);
    }

    public void refreshUsers(ArrayList<User> userList) {
        gameclient.setListUsers(userList);
    }

    // Action listener GameClient
    class CreateRoomListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = gameclient.getInforRoom();
            Client.Send(msg);

            ArrayList<String> roomInfor = (ArrayList<String>) msg.content;

            initGameFrame(gameclient.getUserName(), roomInfor.get(1), roomInfor.get(2), "", true);
            gameFrame.isRoomOwner = true;
            initGame(true);
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

    class ExitListner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Client.Send(new Message(Message.Message_Type.ExitRoom));
            gameFrame.isRoomOwner = false;
            closeGame();

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
                Game.drag = false;
                game.active = null;
                game.selectPiece(x, y);
                gamePanel.revalidate();
                gamePanel.repaint();
            }

        }

//        @Override
//        public void mouseMoved(MouseEvent e) {
//            // temp index i and j for the gui
//            ti = e.getX() / Piece.size;
//            tj = e.getY() / Piece.size;
//            if (Game.board.getPiece(ti, tj) != null) {
//                setCursor(new Cursor(Cursor.HAND_CURSOR));
//            } else {
//                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            }
//            revalidate();
//            repaint();
//        }
//        @Override
//        public void mouseDragged(MouseEvent e) {
//            if (!Game.drag && game.active != null) {
//                game.active = null;
//            }
//            if (SwingUtilities.isLeftMouseButton(e)) {
//                game.selectPiece(e.getX() / Piece.size, e.getY() / Piece.size);
//                Game.drag = true;
//                xx = e.getX();
//                yy = e.getY();
//            }
//            revalidate();
//            repaint();
//        }
        @Override
        public void mouseReleased(MouseEvent e) {
            int x = e.getX() / Piece.size;
            int y = e.getY() / Piece.size;
            game.move(x, y);
            timer.PauseTimer();
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
