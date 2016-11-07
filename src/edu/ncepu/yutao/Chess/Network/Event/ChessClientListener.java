package edu.ncepu.yutao.Chess.Network.Event;

import edu.ncepu.yutao.Chess.DataStructure.ChessClientList;
import edu.ncepu.yutao.Chess.DataStructure.ChessPlayer;

import java.util.EventListener;

/**
 * Created by AUTOY on 2016/6/4.
 */
public interface ChessClientListener extends EventListener {
    void clientListChanged(ChessClientList clientList);

    void moveReceived(String string);

    void chatReceived(ChessPlayer chessPlayer, String s);

    void plateReceived(String string);

    void gameStartReceived(String string);

    void gameOverReceived(String s);

    void joinAknReceived();

    void revokeAskReceived();

    void revokeEnactReceived(int i);
}
