package edu.ncepu.yutao.Chess.Network;

/**
 * Created by AUTOY on 2016/5/7.
 */
public interface ChessServerMessage {
    String CHAT = "CHAT";
    String CHESS_PLATE = "PLATE";
    String MOVE = "MOVE";
    String PEER_CHANGE = "CHANGE";
    String PEER_LEAVE = "LEAVE";
    String AKN_JOIN = "AKN_JOIN";
    String CHESS_START = "CHESS_START";
    String CHESS_OVER = "CHESS_OVER";
    String REVOKE_ASK = "REVOKE_ASK";
    String REVOKE_ENACT = "REVOKE_ENACT";
    String PLATE_INITIAL = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    int ROOM_SERVER_START = 12000;
}
