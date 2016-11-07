package edu.ncepu.yutao.Chess.Network;


import edu.ncepu.yutao.Chess.DataStructure.ChessClientList;
import edu.ncepu.yutao.Chess.DataStructure.ChessClientRecord;
import edu.ncepu.yutao.Chess.DataStructure.ChessPlayer;
import edu.ncepu.yutao.Chess.Network.Event.ChessClientListener;
import edu.ncepu.yutao.Network.Connection;
import edu.ncepu.yutao.Network.DataStructure.PeerRecord;
import edu.ncepu.yutao.Network.DataStructure.RoomRecord;
import edu.ncepu.yutao.Persistence.PersistenceManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by AUTOY on 2016/5/5.
 */
public class ChessClient extends Thread implements ChessClientInterface {
    private DatagramSocket socket;
    private int port;
    private RoomRecord roomRecord;
    private ChessClientRecord clientRecord = null;
    private ChessClientList clientList = new ChessClientList();
    private ChessClientListener listener = null;
    private boolean gameOn = false;
    private boolean flag = true;

    public ChessClient() {
        port = ROOM_CLIENT_START;
        while (!trySocket(port)) {
            port++;
        }
        clientRecord = new ChessClientRecord(Connection.getAddress(), PersistenceManager.getUserName(), port, ChessPlayer.BLANK);
    }

    private boolean trySocket(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    public void setRoomClientListener(ChessClientListener l) {
        listener = l;
    }

    private void fireClientListChanged() {
        listener.clientListChanged(clientList);
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
        System.out.println("客户端线程关闭");
    }

    private void parseCommand(String string) {

        String[] strings = string.split("\\|");
        System.out.println("ChessClient Received:" + string);
        switch (strings[0]) {
            case ChessServerMessage.CHAT:
                if (listener != null) {
                    listener.chatReceived(ChessPlayer.readString(strings[1]), strings[2]);
                }
                break;
            case ChessServerMessage.MOVE:
                if (listener != null) {
                    listener.moveReceived(strings[1]);
                }
                break;
            case ChessServerMessage.CHESS_PLATE:
                if (listener != null) {
                    listener.plateReceived(strings[1]);
                }
                break;
            case ChessServerMessage.CHESS_START:
                if (listener != null) {
                    gameOn = true;
                    listener.gameStartReceived(clientRecord.type.toString());
                }
                break;
            case ChessServerMessage.CHESS_OVER:
                if (listener != null) {
                    gameOn = false;
                    listener.gameOverReceived(strings[1]);
                }
                break;
            case ChessServerMessage.PEER_CHANGE:
                peerChangeReceived(ChessClientRecord.readString(strings, 1));
                break;
            case ChessServerMessage.PEER_LEAVE:
                peerLeaveReceived(ChessClientRecord.readID(strings, 1));
                break;
            case ChessServerMessage.AKN_JOIN:
                aknReceived(ChessClientRecord.readString(strings, 1));
                break;
            case ChessServerMessage.REVOKE_ASK:
                if (listener != null) {
                    listener.revokeAskReceived();
                }
                break;
            case ChessServerMessage.REVOKE_ENACT:
                if (listener != null) {
                    listener.revokeEnactReceived(Integer.parseInt(strings[1]));
                }
                break;
            case Connection.SHUT_DOWN:
                flag = false;
                socket.close();
                break;
        }
    }

    private void aknReceived(ChessClientRecord record) {
        if (record == null) {
            return;
        }
        clientRecord.type = record.type;
        if (listener != null) {
            listener.joinAknReceived();
        }
    }

    private void peerChangeReceived(ChessClientRecord record) {
        if (record == null) {
            return;
        }
        clientList.put(record);
        fireClientListChanged();
        listener.chatReceived(ChessPlayer.DEFAULT, "房间消息：" + record.name + "加入了游戏,作为" + record.type);

    }

    private void peerLeaveReceived(PeerRecord record) {
        if (record == null) {
            return;
        }
        clientList.remove(record.getId());
        fireClientListChanged();
    }

    private void sendDatagramMessage(String command, String message) {
        try {
            Connection.sendDatagramMessage(command + "|" + message, socket, roomRecord.address, roomRecord.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ChessClient Sent    :" + command + "|" + message);
    }

    public boolean joinRoom(RoomRecord server) {
        this.roomRecord = server;
        sendDatagramMessage(JOIN, clientRecord.toString());
        return true;
    }

    public void sendChatMessage(String message) {
        sendDatagramMessage(CHAT, clientRecord.type + "|" + clientRecord.name + ":" + message);
    }

    public void sendMoveMessage(String s) {
        sendDatagramMessage(MOVE, clientRecord.getId() + "|" + s);
    }

    public void leaveRoom() {
        sendDatagramMessage(LEAVE, clientRecord.getId());
        try {
            Connection.sendDatagramMessage(Connection.SHUT_DOWN, socket, clientRecord.address, clientRecord.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChessClientRecord getClientRecord() {
        return clientRecord;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public void sendRevokeRequest() {
        sendDatagramMessage(REVOKE_RQS, clientRecord.getId());
    }

    public void sendRevokeAcknowledge() {
        sendDatagramMessage(REVOKE_ACK, clientRecord.getId());
    }

    public void sendRevokeDenial() {
        sendDatagramMessage(REVOKE_DENY, clientRecord.getId());
    }

    public void sendYieldMessage() {
        sendDatagramMessage(YIELD, clientRecord.getId());
    }
}

