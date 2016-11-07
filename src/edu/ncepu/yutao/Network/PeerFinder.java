package edu.ncepu.yutao.Network;

import edu.ncepu.yutao.Network.DataStructure.PeerList;
import edu.ncepu.yutao.Network.DataStructure.PeerRecord;
import edu.ncepu.yutao.Network.DataStructure.RoomList;
import edu.ncepu.yutao.Network.DataStructure.RoomRecord;
import edu.ncepu.yutao.Network.Event.FinderServerInterface;
import edu.ncepu.yutao.Network.Event.PeerFinderListener;
import edu.ncepu.yutao.Network.Event.ServerListener;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Vector;

;

/**
 * Created by AUTOY on 2016/5/5.
 */
public class PeerFinder extends Thread implements PeerFinderInterface, ServerListener {
    private EventListenerList listenerList = new EventListenerList();
    private Vector<FinderServerInterface> serverList = new Vector<>();
    private PeerList peerList = new PeerList();
    private RoomList roomList = new RoomList();
    private MulticastSocket socket;
    private DatagramSocket datagramSocket;
    private InetAddress group;
    private int multiPort;
    private int idPort;
    private PeerRecord peerRecord = null;

    public PeerFinder() {
        multiPort = PEER_FINDER_START;
        while (!tryMulticastSocket(multiPort)) {
            multiPort++;
        }
        idPort = PEER_FINDER_ID_START;
        while (!tryDatagramSocket(idPort)) {
            idPort++;
        }
        try {
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function is to dynamically allocate multiPort to MulticastSocket simply by try out each possible multiPort
     *
     * @param port is the multiPort number
     * @return whether this operation has been done;
     */
    private boolean tryMulticastSocket(int port) {
        try {
            socket = new MulticastSocket(port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean tryDatagramSocket(int port) {
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    ///////////////////////////////EventHandling////////////////////////////////
    public void addPeerFinderListener(PeerFinderListener l) {
        listenerList.add(PeerFinderListener.class, l);
    }

    public void removePeerFinderListener(PeerFinderListener l) {
        listenerList.remove(PeerFinderListener.class, l);
    }

    public void addServer(FinderServerInterface server) {
        serverList.add(server);
    }

    public void removeServer(FinderServerInterface server) {
        serverList.remove(server);
    }


    protected void firePeerChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PeerFinderListener.class) {
                ((PeerFinderListener) listeners[i + 1]).peerChanged(peerList);
            }
        }
    }

    protected void fireRoomChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PeerFinderListener.class) {
                ((PeerFinderListener) listeners[i + 1]).roomChanged(roomList);
            }
        }
    }

    protected void fireChatReceived(String s) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PeerFinderListener.class) {
                ((PeerFinderListener) listeners[i + 1]).chatReceived(s);
            }
        }
    }


    public void run() {
        int length = 1024;
        byte[] buf = new byte[length];
        try {
            while (true) {
                DatagramPacket dp = new DatagramPacket(buf, length);
                socket.receive(dp);
                String string = new String(dp.getData(), 0, dp.getLength(), "UTF8");
                parseCommand(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCommand(String string) throws UnsupportedEncodingException {

        String[] strings = string.split("\\|");
        System.out.println("PeerFinder  Received:" + string);
        switch (strings[0]) {
            case PEER_KNOCK:
                peerKnockReceived();
                break;
            case PEER_CHANGE:
                peerChangeReceived(PeerRecord.readString(strings, 1));
                break;
            case PEER_LEAVE:
                peerLeaveReceived(PeerRecord.readID(strings, 1));
                break;
            case ROOM_KNOCK:
                roomKnockReceived();
                break;
            case ROOM_CHANGE:
                roomChangeReceived(RoomRecord.readString(strings, 1));
                break;
            case ROOM_LEAVE:
                roomLeaveReceived(RoomRecord.readID(strings, 1));
                break;
            case UNIVERSAL_CHAT:
                universalChatReceived(string.substring(UNIVERSAL_CHAT.length() + 1));
                break;
        }
    }

    private void universalChatReceived(String s) {
        fireChatReceived(s);
    }

    private void roomLeaveReceived(RoomRecord record) {
        roomList.remove(record.getId());
        fireRoomChanged();
    }

    private void roomChangeReceived(RoomRecord record) {
        if (record != null) {
            roomList.put(record);
            fireRoomChanged();
            fireChatReceived("收到房间消息+" + record.toString());
        }
    }

    private void roomKnockReceived() {
        serverList.forEach(e -> sendMulticastMessage(ROOM_CHANGE, e.getRoomRecord().toString()));
    }

    private void peerLeaveReceived(PeerRecord record) {
        peerList.remove(record.getId());
        firePeerChanged();
    }

    private void peerChangeReceived(PeerRecord record) {
        if (record != null) {
            peerList.put(record);
            firePeerChanged();
            fireChatReceived(record.toString() + " 加入了游戏");
        }
    }

    private void peerKnockReceived() {
        if (peerRecord != null) {
            sendMulticastMessage(PEER_CHANGE, peerRecord.toString());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    private void sendMulticastMessage(String command, String s) {
        try {
            Connection.sendMulticastMessage(command + "|" + s, socket, group, multiPort);
            System.out.println("PeerFinder  Sent    :" + command + "|" + s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendPeerChanged(String s) {
        sendMulticastMessage(PEER_CHANGE, s);
    }

    public void sendPeerLeave() {
        sendMulticastMessage(PEER_LEAVE, peerRecord.getId());
    }

    public void sendRoomLeave(String s) {
        sendMulticastMessage(ROOM_LEAVE, s);
    }

    public void sendChatMessage(String s) {
        sendMulticastMessage(UNIVERSAL_CHAT, s);
    }

    public void setPeerRecord(InetAddress address, String name) {
        this.peerRecord = new PeerRecord(address, name, idPort);
    }

    public void startRunning() {
        start();
        sendMulticastMessage(PEER_KNOCK, peerRecord.toString());
        sendMulticastMessage(ROOM_KNOCK, peerRecord.toString());
    }

    @Override
    public void roomRecordChanged(RoomRecord record) {
        sendMulticastMessage(ROOM_CHANGE, record.toString());
    }

    @Override
    public void roomDestroyed(RoomRecord record) {
        sendRoomLeave(record.toString());

    }

    public void onClose() {
        sendPeerLeave();
        serverList.forEach(record -> sendRoomLeave(record.getRoomRecord().getId()));
    }

    public RoomRecord getServerRecord(String s) {
        return roomList.getServerInformation(s);
    }
}

