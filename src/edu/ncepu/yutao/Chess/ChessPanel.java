package edu.ncepu.yutao.Chess;


import edu.ncepu.yutao.Chess.DataStructure.ChessClientList;
import edu.ncepu.yutao.Chess.DataStructure.ChessPlayer;
import edu.ncepu.yutao.Chess.Network.ChessClient;
import edu.ncepu.yutao.Chess.Network.ChessServer;
import edu.ncepu.yutao.Chess.Network.Event.ChessClientListener;
import edu.ncepu.yutao.Chess.UI.ChessBoardPanel;
import edu.ncepu.yutao.Chess.UI.ChessPlayerPanel;
import edu.ncepu.yutao.Chess.UI.Event.ChessPlateListener;
import edu.ncepu.yutao.MainClass.Event.RoomInterface;
import edu.ncepu.yutao.MainClass.Event.RoomListener;
import edu.ncepu.yutao.PublicComponent.ChatPanel;
import edu.ncepu.yutao.PublicComponent.Event.ChatPanelListener;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by AUTOY on 2016/4/26.
 * this class is the main GUI frame of this application
 */
public class ChessPanel extends JPanel implements ChessClientListener, ChatPanelListener, ChessPlateListener, RoomInterface {
    private ChessBoardPanel chessBoardPanel;
    private ChatPanel chatPanel;
    private ChessPlayerPanel playerPanel;
    private ChessClient chessClient = null;
    private ChessServer chessServer = null;
    private RoomListener roomListener = null;
    private JButton revokeButton, yieldButton, exitButton;

    public ChessPanel() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(revokeButton = new JButton("悔棋"));
        revokeButton.addActionListener(e -> onRevokeMove());
        revokeButton.setEnabled(false);
        toolBar.add(yieldButton = new JButton("认输"));
        yieldButton.addActionListener(e -> onYield());
        yieldButton.setEnabled(false);
        toolBar.add(exitButton = new JButton("退出"));
        exitButton.addActionListener(e -> onExeunt());
        toolBar.setFloatable(false);
        this.setSize(870, 700);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        chessBoardPanel = new ChessBoardPanel();
        chessBoardPanel.setChessPlateListener(this);
        playerPanel = new ChessPlayerPanel();
        chatPanel = new ChatPanel();
        chatPanel.addChatPanelListener(this);

        this.add(chessBoardPanel);
        this.add(toolBar);
        this.add(playerPanel);
        this.add(chatPanel);


        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridheight = 1;
        s.gridwidth = 0;
        layout.setConstraints(toolBar, s);

        s = new GridBagConstraints();
        s.fill = GridBagConstraints.NONE;
        s.gridheight = 3;
        s.gridwidth = 1;
        s.weightx = 0.5;
        s.weighty = 0.5;
        s.ipadx = 588;
        s.ipady = 650;
        layout.setConstraints(chessBoardPanel, s);

        s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridheight = 1;
        s.gridwidth = 0;
        s.weighty = 0.5;
        s.weightx = 0.5;
        s.ipadx = 200;
        layout.setConstraints(playerPanel, s);

        s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridheight = 1;
        s.gridwidth = 0;
        s.weighty = 0.5;
        s.weightx = 0.5;
        s.ipadx = 200;
        layout.setConstraints(chatPanel, s);
        this.setVisible(true);
    }

    private void onExeunt() {
        if (chessClient.getClientRecord().type != ChessPlayer.BLANK && chessClient.isGameOn()) {
            if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this,
                    "退出房间将视为认输，是否继续？", "是否退出房间", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE, ImageManager.icon)) {
                return;
            }
        }
        chessClient.leaveRoom();
        if (chessServer != null)
            chessServer.stopRunning();
        roomListener.leaveRoom(this);
        /*try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }*/
    }

    private void onYield() {
        chessClient.sendYieldMessage();

    }

    private void onRevokeMove() {
        chessClient.sendRevokeRequest();
    }

    public ChessClient getChessClient() {
        return chessClient;
    }

    public void setChessClient(ChessClient chessClient) {
        this.chessClient = chessClient;
        chessClient.setRoomClientListener(this);
    }

    public ChessServer getChessServer() {
        return chessServer;
    }

    public void setChessServer(ChessServer chessServer) {
        this.chessServer = chessServer;
    }

    public void setRoomListener(RoomListener listener) {
        this.roomListener = listener;
    }

    @Override
    public void clientListChanged(ChessClientList clientList) {
        playerPanel.updateTable(clientList);
    }

    @Override
    public void moveReceived(String string) {
        chessBoardPanel.handleMove(string);
        chatPanel.appendLn("对方落子:" + string, SubstanceLookAndFeel.getCurrentSkin().getActiveColorScheme(DecorationAreaType.NONE).getForegroundColor());
        roomListener.setBusy(this);
    }

    @Override
    public void chatReceived(ChessPlayer chessPlayer, String s) {
        switch (chessPlayer) {
            case BLUE:
                chatPanel.appendLn(s, Color.BLUE);
                break;
            case RED:
                chatPanel.appendLn(s, Color.RED);
                break;
            case BLANK:
                chatPanel.appendLn(s, Color.GRAY);
                break;
            case DEFAULT:
                chatPanel.appendLn(s, Color.magenta);
        }
    }

    @Override
    public void plateReceived(String string) {
        chessBoardPanel.readFromString(string);
    }

    @Override
    public void gameStartReceived(String string) {
        yieldButton.setEnabled(true);
        chessBoardPanel.handleStart(string);
    }

    @Override
    public void gameOverReceived(String s) {
        chessBoardPanel.setMovable(false);
        yieldButton.setEnabled(false);
        revokeButton.setEnabled(false);
        if (s.equals(ChessPlayer.RED.toString())) {
            chatPanel.appendLn("红方取得了胜利！！！", Color.red);
        } else if (s.equals(ChessPlayer.BLUE.toString())) {
            chatPanel.appendLn("蓝方取得了胜利！！！", Color.blue);
        }
    }

    @Override
    public void joinAknReceived() {
    }

    @Override
    public void revokeAskReceived() {
        SwingUtilities.invokeLater(() -> {
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
                    "对方发出了悔棋请求，是否同意？", "悔棋请求", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE, ImageManager.icon)) {
                chessClient.sendRevokeAcknowledge();
            } else {
                chessClient.sendRevokeDenial();
            }
        });
    }

    @Override
    public void revokeEnactReceived(int i) {
        chessBoardPanel.revokeMove(i);
    }

    @Override
    public void messageSent(String message) {
        if (chessClient != null) {
            chessClient.sendChatMessage(message);
        }
    }

    @Override
    public void chessMoved(String lastMove) {
        if (chessClient != null) {
            revokeButton.setEnabled(true);
            chessClient.sendMoveMessage(lastMove);
        }
    }

    @Override
    public void revokeUsedUp() {
        revokeButton.setEnabled(false);
    }
/////////////////////////////////////////////////////////////
}

