package edu.ncepu.yutao.Chess.Network;

import edu.ncepu.yutao.Chess.DataStructure.ChessClientList;
import edu.ncepu.yutao.Chess.DataStructure.ChessClientRecord;
import edu.ncepu.yutao.Chess.DataStructure.ChessHistory;
import edu.ncepu.yutao.Chess.DataStructure.ChessPlayer;
import edu.ncepu.yutao.Network.Connection;
import edu.ncepu.yutao.Network.DataStructure.PeerRecord;
import edu.ncepu.yutao.Network.DataStructure.RoomRecord;
import edu.ncepu.yutao.Network.Event.FinderServerInterface;
import edu.ncepu.yutao.Network.Event.ServerListener;
import edu.ncepu.yutao.Persistence.PersistenceManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by AUTOY on 2016/5/5.
 */
public class ChessServer extends Thread implements ChessServerMessage, FinderServerInterface {
    private DatagramSocket socket;
    private ChessClientList clientList = new ChessClientList();
    private ChessClientRecord clientRed = null;
    private ChessClientRecord clientBlue = null;
    private ChessClientRecord revoker = null;
    private ChessClientRecord acknowledger = null;
    private RoomRecord roomRecord = null;
    private ChessHistory history = new ChessHistory();
    private ServerListener listener = null;
    private boolean flag = true;
    private boolean gameOn = false;

    public ChessServer() {
        int port = ROOM_SERVER_START;
        while (!trySocket(port)) {
            port++;
        }
        roomRecord = new RoomRecord(Connection.getAddress(), port, PersistenceManager.getUserName(), "象棋", 0);
    }

    private boolean trySocket(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    public void setChessServerListener(ServerListener listener) {
        this.listener = listener;
    }

    private void fireRoomRecordChanged() {
        listener.roomRecordChanged(roomRecord);
    }

    public void run() {
        flag = true;
        int length = 1024;
        byte[] buf = new byte[length];
        DatagramPacket dp;
        try {
            while (flag) {
                dp = new DatagramPacket(buf, length);
                socket.receive(dp);
                String string = new String(dp.getData(), 0, dp.getLength(), "UTF8");
                parseCommand(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("服务端线程关闭");
    }

    private void parseCommand(String string) {
        String[] strings = string.split("\\|");
        //System.out.println("ChessServer Received:" + string);
        switch (strings[0]) {
            case ChessClientInterface.CHAT:
                chatReceived(string);
                break;
            case ChessClientInterface.JOIN:
                joinReceived(PeerRecord.readString(strings, 1));
                break;
            case ChessClientInterface.MOVE:
                moveReceived(PeerRecord.readID(strings, 1), strings[3]);
                break;
            case ChessClientInterface.LEAVE:
                leaveReceived(PeerRecord.readID(strings, 1));
                break;
            case ChessClientInterface.READY:
                break;
            case ChessClientInterface.REVOKE_ACK:
                revokeAcknowledgeReceived(PeerRecord.readID(strings, 1));
                break;
            case ChessClientInterface.REVOKE_DENY:
                revokeDenialReceived(PeerRecord.readID(strings, 1));
                break;
            case ChessClientInterface.REVOKE_RQS:
                revokeRequestReceived(PeerRecord.readID(strings, 1));
                break;
            case ChessClientInterface.YIELD:
                yieldReceived(PeerRecord.readID(strings, 1));
                break;
            case Connection.SHUT_DOWN:
                flag = false;
                socket.close();
                break;
        }
    }

    private void yieldReceived(PeerRecord record) {
        ChessClientRecord record1;
        ChessPlayer winner;
        if (record.equals(clientRed)) {
            record1 = clientRed;
            winner = ChessPlayer.BLUE;
        } else if (record.equals(clientBlue)) {
            record1 = clientBlue;
            winner = ChessPlayer.RED;
        } else return;

        sendToAll(CHAT + "|" + record1.type + "|" + record1.type + "投降了");
        sendToAll(CHESS_OVER + "|" + winner);

    }

    private void revokeDenialReceived(PeerRecord record) {
        if (record.equals(acknowledger)) {
            sendToAll(CHAT + "|悔棋收到了拒绝");
        }
    }

    private void revokeRequestReceived(PeerRecord record) {
        if (record.equals(clientRed)) {
            revoker = clientRed;
            acknowledger = clientBlue;
        } else {
            revoker = clientBlue;
            acknowledger = clientRed;
        }
        sendDatagramMessage(REVOKE_ASK, acknowledger);
        sendToAll(CHAT + "|" + revoker.type + "|" + revoker.name + "发起了悔棋请求");
    }

    private void revokeAcknowledgeReceived(PeerRecord record) {
        if (record.equals(acknowledger)) {
            sendToAll(REVOKE_ENACT + "|" + history.revokeLast(revoker.type));
            sendToAll(CHAT + "|" + acknowledger.type + "|悔棋得到了同意");
        }
    }

    private void leaveReceived(PeerRecord record) {
        clientList.remove(record.getId());
        roomRecord.playerCount = clientList.getCount();
        if (gameOn) {
            if (record.equals(clientRed)) {
                sendToAll(CHESS_OVER + "|" + ChessPlayer.BLUE.toString());
            } else if (record.equals(clientBlue)) {
                sendToAll(CHESS_OVER + "|" + ChessPlayer.RED.toString());
            }
        }
        sendToAll(PEER_LEAVE + "|" + record.getId());
        if (clientList.getCount() != 0) {
            fireRoomRecordChanged();
        }
    }

    private void chatReceived(String message) {
        sendToAll(message);
    }

    private void joinReceived(PeerRecord record) {
        if (record == null) {
            return;
        }
        ChessClientRecord newRecord;
        int size = clientList.getCount();
        if (size == 0) {
            newRecord = clientRed = new ChessClientRecord(record, ChessPlayer.RED);
        } else if (size == 1) {
            newRecord = clientBlue = new ChessClientRecord(record, ChessPlayer.BLUE);
        } else {
            newRecord = new ChessClientRecord(record, ChessPlayer.BLANK);
        }
        sendDatagramMessage(AKN_JOIN + "|" + newRecord.toString(), newRecord);
        clientList.getCollection().forEach(info -> sendDatagramMessage(PEER_CHANGE + "|" + info.toString(), newRecord));
        clientList.put(newRecord);
        sendToAll(PEER_CHANGE + "|" + newRecord.toString());


        if (clientList.getCount() == 2) {
            //start game
            history.readFromString(PLATE_INITIAL);
            sendToAll(CHESS_START + "|" + PLATE_INITIAL);
        } else if (clientList.getCount() > 2) {
            //send current chess plate
            sendDatagramMessage(CHESS_PLATE + "|" + history.saveToString(), newRecord);
        }
        roomRecord.playerCount = clientList.getCount();
        fireRoomRecordChanged();
    }

    private void moveReceived(PeerRecord record, String move) {
        if (record == null) {
            return;
        }
        ChessPlayer kingSlain = history.readMove(move);
        clientList.getCollection().stream().filter(info -> !record.equals(info)).forEach(info -> sendDatagramMessage(MOVE + "|" + move, info));
        if (kingSlain == ChessPlayer.BLUE) {
            sendToAll(CHESS_OVER + "|" + ChessPlayer.RED);
        } else if (kingSlain == ChessPlayer.RED) {
            sendToAll(CHESS_OVER + "|" + ChessPlayer.BLUE);
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void sendDatagramMessage(String s, ChessClientRecord record) {
        try {
            Connection.sendDatagramMessage(s, socket, record.address, record.port);
            //System.out.println("ChessServer Sent    :" + s +"\n        Destination:"+record.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToAll(String s) {
        clientList.getCollection().forEach(info -> sendDatagramMessage(s, info));
    }

    public void stopRunning() {
        listener.roomDestroyed(roomRecord);
        try {
            Connection.sendDatagramMessage(Connection.SHUT_DOWN, socket, roomRecord.address, roomRecord.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RoomRecord getRoomRecord() {
        return roomRecord;
    }
}


