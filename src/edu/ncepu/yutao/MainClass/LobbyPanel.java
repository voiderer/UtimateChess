package edu.ncepu.yutao.MainClass;

import edu.ncepu.yutao.Chess.SoundThread;
import edu.ncepu.yutao.MainClass.Event.LobbyListener;
import edu.ncepu.yutao.Network.DataStructure.PeerList;
import edu.ncepu.yutao.Network.DataStructure.RoomList;
import edu.ncepu.yutao.Network.Event.PeerFinderListener;
import edu.ncepu.yutao.Network.PeerFinder;
import edu.ncepu.yutao.Persistence.PersistenceManager;
import edu.ncepu.yutao.PublicComponent.ChatPanel;
import edu.ncepu.yutao.PublicComponent.Event.ChatPanelListener;
import edu.ncepu.yutao.PublicComponent.MusicManager;
import edu.ncepu.yutao.PublicComponent.PlayerPanel;
import edu.ncepu.yutao.PublicComponent.RoomPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by AUTOY on 2016/6/4.
 */
public class LobbyPanel extends JPanel implements PeerFinderListener, ChatPanelListener {
    private ChatPanel chatPanel = new ChatPanel();
    private PlayerPanel playerPanel = new PlayerPanel();
    private RoomPanel roomPanel = new RoomPanel();
    private JButton musicButton;
    private JButton soundButton;
    //private UserPanel userPanel = new UserPanel();
    private PeerFinder peerFinder = null;
    private LobbyListener listener = null;

    public LobbyPanel() {
        SwingUtilities.invokeLater(() -> {
            JToolBar toolBar = new JToolBar();
            JButton button;
            toolBar.add(button = new JButton("创建房间"));
            button.addActionListener(e -> onMakeChessRoom());
            toolBar.add(button = new JButton("加入房间"));
            button.addActionListener(e -> onJoinChessRoom());
            toolBar.add(musicButton = new JButton("Music"));
            musicButton.addActionListener(e -> onSwitchMusic());
            toolBar.add(soundButton = new JButton("Sound"));
            soundButton.addActionListener(e -> onSwitchSound());
            toolBar.setFloatable(false);
            BorderLayout layout;
            this.setLayout(layout = new BorderLayout());
            this.add(toolBar, BorderLayout.NORTH);
            this.add(roomPanel, BorderLayout.CENTER);
            this.add(playerPanel, BorderLayout.WEST);
            //this.add(chatPanel, BorderLayout.SOUTH);
            this.add(chatPanel, BorderLayout.EAST);
            layout.setHgap(2);
            layout.setVgap(2);

            chatPanel.addChatPanelListener(this);
        });
    }

    private void onSwitchSound() {
        if (SoundThread.switchEnable()) {
            soundButton.setText("off");
        } else {
            soundButton.setText("on");
        }
    }

    private void onSwitchMusic() {
        if (MusicManager.switchMusic()) {
            musicButton.setText("TurnOn");
        } else {
            musicButton.setText("TurnOff");
        }
    }

    private void onJoinChessRoom() {
        if (listener != null) {
            String s = roomPanel.getSelected();
            if (s.equals("")) {
                JOptionPane.showMessageDialog(this,
                        "请选中一个房间!", "系统信息", JOptionPane.ERROR_MESSAGE);
                return;
            }
            listener.joinChessRoom(peerFinder.getServerRecord(s));
        }
    }

    public void setPeerFinder(PeerFinder peerFinder) {
        this.peerFinder = peerFinder;
    }

    public void setLobbyController(LobbyListener listener) {
        this.listener = listener;
    }

    @Override
    public void peerChanged(PeerList list) {
        playerPanel.updateTable(list);
    }

    @Override
    public void roomChanged(RoomList list) {
        roomPanel.updateTable(list);
    }

    @Override
    public void chatReceived(String s) {
        chatPanel.appendLn(s, Color.BLACK);
    }

    @Override
    public void messageSent(String message) {
        peerFinder.sendChatMessage(PersistenceManager.getUserName() + ":" + message);
    }

    private void onMakeChessRoom() {
        if (listener != null) {
            listener.makeChessRoom();
        }
    }
}
