package edu.ncepu.yutao.MainClass.Event;

import edu.ncepu.yutao.Chess.ChessPanel;

/**
 * Created by AUTOY on 2016/6/10.
 */
public interface RoomListener {
    void leaveRoom(Object room);

    void setBusy(ChessPanel panel);
}
