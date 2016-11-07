package edu.ncepu.yutao.Chess.UI.Event;

/**
 * Created by AUTOY on 2016/6/10.
 */
public interface ChessPlateListener {
    void chessMoved(String lastMove);

    void revokeUsedUp();
}
