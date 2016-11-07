package edu.ncepu.yutao.Chess.Network;

/**
 * Created by AUTOY on 2016/5/7.
 */
public interface ChessClientInterface {
    String CHAT = "CHAT";
    String JOIN = "JOIN";
    String READY = "READY";
    String LEAVE = "LEAVE";
    String YIELD = "YIELD";
    String REVOKE_RQS = "REVOKE_RQS";
    String REVOKE_ACK = "REVOKE_ACK";
    String REVOKE_DENY = "REVOKE_DENY";
    String MOVE = "MOVE";
    int ROOM_CLIENT_START = 14000;
}
