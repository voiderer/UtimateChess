package edu.ncepu.yutao.MainClass;

import edu.ncepu.yutao.Chess.ChessPanel;
import edu.ncepu.yutao.Chess.ImageManager;
import edu.ncepu.yutao.Chess.Network.ChessClient;
import edu.ncepu.yutao.Chess.Network.ChessServer;
import edu.ncepu.yutao.Chess.SoundManifest;
import edu.ncepu.yutao.MainClass.Event.LobbyListener;
import edu.ncepu.yutao.MainClass.Event.RoomListener;
import edu.ncepu.yutao.Network.Connection;
import edu.ncepu.yutao.Network.DataStructure.RoomRecord;
import edu.ncepu.yutao.Network.PeerFinder;
import edu.ncepu.yutao.Persistence.PersistenceManager;
import edu.ncepu.yutao.PublicComponent.MusicManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Created by AUTOY on 2016/6/2.
 */
public class MainWindow extends JFrame implements LobbyListener, RoomListener {
    private JTabbedPane tabbedPane;
    private PeerFinder peerFinder = new PeerFinder();
    private LobbyPanel lobbyPanel = new LobbyPanel();
    private Vector<Object> panels = new Vector<>();

    public MainWindow() {
        super("局域网游戏-" + PersistenceManager.getUserName());
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        this.setContentPane(tabbedPane);
        peerFinder.addPeerFinderListener(lobbyPanel);
        peerFinder.setPeerRecord(Connection.getAddress(), PersistenceManager.getUserName());
        peerFinder.startRunning();
        tabbedPane.addTab("游戏大厅", lobbyPanel);
        MusicManager.PlayMusic(SoundManifest.GAME_BACK);
        panels.add(lobbyPanel);
        lobbyPanel.setPeerFinder(peerFinder);
        lobbyPanel.setLobbyController(this);
        this.setSize(990, 770);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        setIconImage(ImageManager.icon.getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosed(e);
                onClose();
            }
        });
    }

    private void onClose() {
        PersistenceManager.saveChessData();
        peerFinder.onClose();
        System.exit(0);
    }

    @Override
    public void makeChessRoom() {
        SwingUtilities.invokeLater(() -> {
            ChessClient client = new ChessClient();
            ChessServer server = new ChessServer();
            ChessPanel panel = new ChessPanel();
            client.setRoomClientListener(panel);
            server.setChessServerListener(peerFinder);
            peerFinder.addServer(server);
            panel.setChessClient(client);
            panel.setChessServer(server);
            panel.setRoomListener(this);
            server.start();
            client.start();
            client.joinRoom(server.getRoomRecord());
            tabbedPane.addTab(">象棋游戏", panel);
            //  tabbedPane.setEnabledAt(0,false);
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            panels.add(panel);
        });
    }

    @Override
    public void joinChessRoom(RoomRecord serverRecord) {
        SwingUtilities.invokeLater(() -> {
            ChessClient client = new ChessClient();
            ChessPanel panel = new ChessPanel();
            client.setRoomClientListener(panel);
            panel.setChessClient(client);
            panel.setRoomListener(this);
            client.start();
            client.joinRoom(serverRecord);
            tabbedPane.addTab(">象棋游戏", panel);
            // tabbedPane.setEnabledAt(0,false);
            tabbedPane.setSelectedComponent(panel);
            panels.add(panel);
        });
    }

    @Override
    public void leaveRoom(Object room) {
        int i = panels.indexOf(room);
        tabbedPane.remove(i);
        panels.remove(room);
        peerFinder.removeServer(((ChessPanel) room).getChessServer());
        //  tabbedPane.setEnabledAt(0,true);
    }

    @Override
    public void setBusy(ChessPanel panel) {
        tabbedPane.setSelectedComponent(panel);
    }
}
